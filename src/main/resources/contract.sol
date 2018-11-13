pragma solidity ^0.4.11;


/**
 * @title Ownable
 * @dev The Ownable contract has an owner address, and provides basic authorization control
 * functions, this simplifies the implementation of "user permissions".
 */
contract Ownable {
  address public owner;


  /**
   * @dev The Ownable constructor sets the original `owner` of the contract to the sender
   * account.
   */
  constructor() public {
    owner = msg.sender;
  }


  /**
   * @dev Throws if called by any account other than the owner.
   */
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }


  /**
   * @dev Allows the current owner to transfer control of the contract to a newOwner.
   * @param newOwner The address to transfer ownership to.
   */
  function transferOwnership(address newOwner) onlyOwner public {
    if (newOwner != address(0)) {
      owner = newOwner;
    }
  }

}



/// @title Interface for contracts conforming to ERC-721: Non-Fungible Tokens
/// @author Dieter Shirley <dete@axiomzen.co> (https://github.com/dete)
contract ERC721 {
    // Required methods
    function totalSupply() public view returns (uint256 total);
    function balanceOf(address _owner) public view returns (uint256 balance);
    function ownerOf(uint256 _tokenId) external view returns (address owner);
    function approve(address _to, uint256 _tokenId) external;
    function transfer(address _to, uint256 _tokenId) external;
    function transferFrom(address _from, address _to, uint256 _tokenId) external;

    // Events
    event Transfer(address from, address to, uint256 tokenId);
    event Approval(address owner, address approved, uint256 tokenId);

    // Optional
     //function name() public view returns (string name);
    // function symbol() public view returns (string symbol);
    //function tokensOfOwner(address _owner) external view returns (uint256[] tokenIds);
    // function tokenMetadata(uint256 _tokenId, string _preferredTransport) public view returns (string infoUrl);

    // ERC-165 Compatibility (https://github.com/ethereum/EIPs/issues/165)
    function supportsInterface(bytes4 _interfaceID) external view returns (bool);
}







/// @title A facet of Ticketcoin that manages special access privileges.
/// @dev See the Ticketcoin contract documentation to understand how the various contract facets are arranged.
contract TicketCoinAccessControl {
    // This facet controls access control for TicketCoin tickets. There are four roles managed here:
    //
    //     - The CEO: The CEO can reassign other roles and change the addresses of our dependent smart
    //         contracts. It is also the only role that can unpause the smart contract. It is initially
    //         set to the address that created the smart contract in the TicketCoin constructor.
    //
    //     - The CFO: The CFO can withdraw funds from TicketCoin and its auction contracts.
    //
    //     - The CTO: The CTO can supervise ticket lifecycle (creation/transfer/burn)
    //
    // It should be noted that these roles are distinct without overlap in their access abilities, the
    // abilities listed for each role above are exhaustive. In particular, while the CEO can assign any
    // address to any role, the CEO address itself doesn't have the ability to act in those roles. This
    // restriction is intentional so that we aren't tempted to use the CEO address frequently out of
    // convenience. The less we use an address, the less likely it is that we somehow compromise the
    // account.

    /// @dev Emited when contract is upgraded 
    event ContractUpgrade(address newContract);

    // The addresses of the accounts (or contracts) that can execute actions within each roles.
    address public ceoAddress;
    address public cfoAddress;
    address public ctoAddress;

    // @dev Keeps track whether the contract is paused. When that is true, most actions are blocked
    bool public paused = false;

    /// @dev Access modifier for CEO-only functionality
    modifier onlyCEO() {
        require(msg.sender == ceoAddress);
        _;
    }

    /// @dev Access modifier for CFO-only functionality
    modifier onlyCFO() {
        require(msg.sender == cfoAddress);
        _;
    }

    /// @dev Access modifier for CTO-only functionality
    modifier onlyCTO() {
        require(msg.sender == ctoAddress);
        _;
    }

    modifier onlyCLevel() {
        require(
            msg.sender == ctoAddress ||
            msg.sender == ceoAddress ||
            msg.sender == cfoAddress
        );
        _;
    }

    /// @dev Assigns a new address to act as the CEO. Only available to the current CEO.
    /// @param _newCEO The address of the new CEO
    function setCEO(address _newCEO) external onlyCEO {
        require(_newCEO != address(0));

        ceoAddress = _newCEO;
    }

    /// @dev Assigns a new address to act as the CFO. Only available to the current CEO.
    /// @param _newCFO The address of the new CFO
    function setCFO(address _newCFO) external onlyCEO {
        require(_newCFO != address(0));

        cfoAddress = _newCFO;
    }

    /// @dev Assigns a new address to act as the CTO. Only available to the current CEO.
    /// @param _newCTO The address of the new CTO
    function setCTO(address _newCTO) external onlyCEO {
        require(_newCTO != address(0));

        ctoAddress = _newCTO;
    }

    /*** Pausable functionality adapted from OpenZeppelin ***/

    /// @dev Modifier to allow actions only when the contract IS NOT paused
    modifier whenNotPaused() {
        require(!paused);
        _;
    }

    /// @dev Modifier to allow actions only when the contract IS paused
    modifier whenPaused {
        require(paused);
        _;
    }

    /// @dev Called by any "C-level" role to pause the contract. Used only when
    ///  a bug or exploit is detected and we need to limit damage.
    function pause() external onlyCLevel whenNotPaused {
        paused = true;
    }

    /// @dev Unpauses the smart contract. Can only be called by the CEO, since
    ///  one reason we may pause the contract is when CFO or COO accounts are
    ///  compromised.
    /// @notice This is public rather than external so it can be called by
    ///  derived contracts.
    function unpause() public onlyCEO whenPaused {
        // can't unpause if contract was upgraded
        paused = false;
    }
}





/// @title Base contract for Tickets. Holds all common structs, events and base variables.
contract TicketBase is TicketCoinAccessControl {
    
    /*** EVENTS ***/


    /// @dev Transfer event as defined in current draft of ERC721. Emitted every time a ticket
    ///  ownership is assigned.
    event Transfer(address from, address to, uint256 tokenId);


    /// @dev The Consume event is fired whenever a ticket is consumed at the gates of the vent.
    event Consume(uint256 ticketId);

    /// @dev The Birth event is fired whenever a ticket is consumed bat the event gate.
    event Cancel(uint256 ticketId);


    /*** DATA TYPES ***/

    /// @dev The main Ticket struct. Every ticket in Ticketcoin is represented by a copy
    ///  of this structure, so great care was taken to ensure that it fits neatly into
    ///  exactly two 256-bit words. Note that the order of the members in this structure
    ///  is important because of the byte-packing rules used by Ethereum.
    ///  Ref: http://solidity.readthedocs.io/en/develop/miscellaneous.html
    struct Ticket {
        // The Ticket unique identifier.
        uint128 ticketUUID;
        
        // The id of the organization that enrolled/owns the ticket
        uint128 organizationUUID;
        
        //the price cap for transfer(expressed in Gwei)
        uint64 priceCapGwai;
      
        // The timestamp from the block when this ticket was enrolled.
        uint32 enrollTime;

         // The date since whom the ticket can be spent.
        uint32 validFrom;       
        
         // The expiration date of the ticket.
        uint32 validUntil;        
        
       // The timestamp from the block when this ticket was consumed
        uint32 consumedTime;

       // The timestamp from the block when this ticket was canceled
        uint32 canceledTime;


        //The number remainig transfers allowed.  -1 means infinite transfers      
        int16 allowedTransfers;
        
        //The status of the ticket
        uint8 ticketState;
        
        //The transfer rule to apply
        uint8 transferRule;
        
    }

    /*** CONSTANTS ***/
    uint8 constant STATE_INVALID = 0;
    uint8 constant STATE_VALID = 1;
    uint8 constant STATE_SPENT = 2;

    uint8 constant TRANSFER_RULE_ANY = 0;
    uint8 constant TRANSFER_RULE_WHITE_LIST_ONLY = 1;


    /*** STORAGE ***/

    /// @dev An array containing the Ticket struct for all Tickets enrolled.
    Ticket[] tickets;

    /// @dev A mapping from ticket IDs to the address that owns them. 
    mapping (uint256 => address) public ticketIndexToOwner;

    // @dev A mapping from owner address to count of tokens that address owns.
    //  Used internally inside balanceOf() to resolve ownership count.
    mapping (address => uint256) ownershipTokenCount;

    /// @dev A mapping from TicketIDs to an address that has been approved to call
    ///  transferFrom(). Each Ticket can only have one approved address for transfer
    ///  at any time. A zero value means no approval is outstanding.
    mapping (uint256 => address) public ticketIndexToApproved;


    // @dev A mapping from ticketUUID to ticketIndex
    mapping (uint256 => uint256) ticketUUIDToIndex;   

   
    // @dev A mapping from owner address to organizationUUID.
    //  Used to grant obliterate and cancel function to organization granted addresses.
    mapping (address => uint256) addressToOrganizationUUID;
    
    // @dev A white list of addresses. (eg. addresses with KYC complete)
    mapping (address => bool) addressWhiteList;
    
    
    TicketPaymentGateway public ticketPaymentGateway;
        

    /// @dev Assigns ownership of a specific ticket to an address.
    function _transfer(address _from, address _to, uint256 _tokenId) internal {
        // Since the number of tickets is capped to 2^32 we can't overflow this
        ownershipTokenCount[_to]++;
        // transfer ownership
        ticketIndexToOwner[_tokenId] = _to;

        if (_from != address(0)) {
            ownershipTokenCount[_from]--;

            // clear any previously approved ownership exchange
            delete ticketIndexToApproved[_tokenId];
        }

        // Emit the transfer event.
        emit Transfer(_from, _to, _tokenId);
    }

    /// @dev Consume the validity of a ticket.
    function _consume(uint256 _tokenId) internal {
        // consume the ticket
        tickets[_tokenId].consumedTime=uint32(now);
        tickets[_tokenId].ticketState = uint8(STATE_SPENT);
        // Emit the transfer event.
        emit Consume(_tokenId);
    }

    /// @dev Cancel the ticket
    function _cancel(uint256 _tokenId) internal {
        // cancel the ticket
        tickets[_tokenId].canceledTime=uint32(now);
        tickets[_tokenId].ticketState = uint8(STATE_INVALID);

        // Emit the transfer event.
        emit Cancel(_tokenId);
    }
    
    


    /// @dev An internal method that creates a new ticket and stores it. This
    ///  method doesn't do any checking and should only be called when the
    ///  input data is known to be valid. Will generate a Transfer event.
    /// @param ticketUUID The ticket ID 
    /// @param _organizationID The organization ID 
    /// @param _eventID The event ID 
    /// @param _transferAllowed The number transfers allowed for this ticket.
    /// @param _owner The inital owner of this ticket, must be non-zero
    function _createTicket(
        uint256 _ticketUUID,
        uint256 _organizationUUID,
        uint256 _priceCapGwai,
        uint256 _validFrom,
        uint256 _validUntil,
        int256 _allowedTransfers,
        uint256 _transferRule,
        uint256 _ticketState,
        address _owner
    )
        internal
        returns (uint)
    {
        

        
        // These requires are not strictly necessary, our calling code should make
        // sure that these conditions are never broken. 
        require(_ticketUUID == uint256(uint128(_ticketUUID)));
        require(_organizationUUID == uint256(uint128(_organizationUUID)));
        require(_priceCapGwai == uint256(uint128(_priceCapGwai)));
        require(_allowedTransfers == int256(int8(_allowedTransfers)));
        require(_transferRule == uint256(uint8(_transferRule)));
        require(_ticketState == uint256(uint8(_ticketState)));        
        
        
   
    //creates the ticket structure
        Ticket memory _ticket = Ticket({
            ticketUUID: uint128(_ticketUUID),
            organizationUUID: uint128(_organizationUUID),
            priceCapGwai: uint64(_priceCapGwai),
            enrollTime: uint32(now),
            validFrom: uint32(_validFrom),
            validUntil: uint32(_validUntil),
            consumedTime: 0,
            canceledTime: 0,
            allowedTransfers: int16(_allowedTransfers),
            ticketState: uint8(STATE_VALID),
            transferRule: uint8(_transferRule)
        });
        
        
        
        
        uint256 newTicketId = tickets.push(_ticket) - 1;

 		//avoid overflow
        require(newTicketId == uint256(uint32(newTicketId)));

        //link the ticketID to ticketUUID
        ticketUUIDToIndex[_ticketUUID]=newTicketId;

        // This will assign ownership, and also emit the Transfer event as
        // per ERC721 draft
        _transfer(0, _owner, newTicketId);

        return newTicketId;
    }


}






/// @title The external contract that is responsible for generating metadata for the tickets,
contract ERC721Metadata {
    /// @dev Given a token Id, returns a byte array that is supposed to be converted into string.
    function getMetadata(uint256 _tokenId, string) public view returns (bytes32[4] buffer, uint256 count) {
        
            buffer[0] = "http://ticketcoin.io/public/tck/";
            buffer[1] = uintToBytes(_tokenId);
            count = 64;
    }
    
 
    ///  This method is licenced under MIT License.
    ///  Ref: https://github.com/pipermerriam/ethereum-string-utils
    function uintToBytes(uint v)  constant  returns (bytes32 ret) {
        if (v == 0) {
            ret = '0';
        }
        else {
            while (v > 0) {
                ret = bytes32(uint(ret) / (2 ** 8));
                ret |= bytes32(((v % 10) + 48) * 2 ** (8 * 31));
                v /= 10;
            }
        }
        return ret;
    }
}


/// @title The facet of the TicketCoin core contract that manages ownership, ERC-721 (draft) compliant.
/// @dev Ref: https://github.com/ethereum/EIPs/issues/721
contract TicketOwnership is TicketBase, ERC721 {

    /// @notice Name and symbol of the non fungible token, as defined in ERC721.
    string public constant name = "TicketCoinTicket";
    string public constant symbol = "TCKT";

    // The contract that will return ticket metadata
    ERC721Metadata public erc721Metadata;

    bytes4 constant InterfaceSignature_ERC165 =
        bytes4(keccak256('supportsInterface(bytes4)'));

    bytes4 constant InterfaceSignature_ERC721 =
        bytes4(keccak256('name()')) ^
        bytes4(keccak256('symbol()')) ^
        bytes4(keccak256('totalSupply()')) ^
        bytes4(keccak256('balanceOf(address)')) ^
        bytes4(keccak256('ownerOf(uint256)')) ^
        bytes4(keccak256('approve(address,uint256)')) ^
        bytes4(keccak256('transfer(address,uint256)')) ^
        bytes4(keccak256('transferFrom(address,address,uint256)')) ^
        bytes4(keccak256('tokensOfOwner(address)')) ^
        bytes4(keccak256('tokenMetadata(uint256,string)'));

    /// @notice Introspection interface as per ERC-165 (https://github.com/ethereum/EIPs/issues/165).
    ///  Returns true for any standardized interfaces implemented by this contract. We implement
    ///  ERC-165 (obviously!) and ERC-721.
    function supportsInterface(bytes4 _interfaceID) external view returns (bool)
    {
        // DEBUG ONLY
        //require((InterfaceSignature_ERC165 == 0x01ffc9a7) && (InterfaceSignature_ERC721 == 0x9a20483d));

        return ((_interfaceID == InterfaceSignature_ERC165) || (_interfaceID == InterfaceSignature_ERC721));
    }
    
    

    /// @dev Set the address of the sibling contract that tracks metadata.
    ///  CEO only.
    function setMetadataAddress(address _contractAddress) public onlyCEO {
        erc721Metadata = ERC721Metadata(_contractAddress);
    }

    // Internal utility functions: These functions all assume that their input arguments
    // are valid. We leave it to public methods to sanitize their inputs and follow
    // the required logic.

    /// @dev Checks if a given address is the current owner of a particular Ticket.
    /// @param _claimant the address we are validating against.
    /// @param _tokenId ticket id, only valid when > 0
    function _owns(address _claimant, uint256 _tokenId) internal view returns (bool) {
        return ticketIndexToOwner[_tokenId] == _claimant;
    }

    /// @dev Checks if a given address currently has transferApproval for a particular Ticket.
    /// @param _claimant the address we are confirming ticket is approved for.
    /// @param _tokenId ticket id, only valid when > 0
    function _approvedFor(address _claimant, uint256 _tokenId) internal view returns (bool) {
        return ticketIndexToApproved[_tokenId] == _claimant;
    }

    /// @dev Marks an address as being approved for transferFrom(), overwriting any previous
    ///  approval. Setting _approved to address(0) clears all transfer approval.
    ///  NOTE: _approve() does NOT send the Approval event. This is intentional because
    ///  _approve() and transferFrom() are used together for putting Ticket on auction, and
    ///  there is no value in spamming the log with Approval events in that case.
    function _approve(uint256 _tokenId, address _approved) internal {
        ticketIndexToApproved[_tokenId] = _approved;
    }

    /// @notice Returns the number of Tickets owned by a specific address.
    /// @param _owner The owner address to check.
    /// @dev Required for ERC-721 compliance
    function balanceOf(address _owner) public view returns (uint256 count) {
        return ownershipTokenCount[_owner];
    }

    /// @notice Transfers a Ticket to another address.
    /// @param _to The address of the recipient
    /// @param _tokenId The ID of the Ticket to transfer.
    /// @dev Required for ERC-721 compliance.
    function transfer(
        address _to,
        uint256 _tokenId
    )
        external
        whenNotPaused
    {
        // Safety check to prevent against an unexpected 0x0 default.
        require(_to != address(0));
        // Disallow transfers to this contract to prevent accidental misuse.
        // The contract should never own any tickets (except very briefly
        // after a gen0 cat is created and before it goes on auction).
        require(_to != address(this));

        // You can only send your own Ticket.
        require(_owns(msg.sender, _tokenId));
        

        //verify that the sendendr is the owner of the message
        //require(_owns(msg.sender, _tokenId));

        //Verify the transfer rule
        require(tickets[_tokenId].transferRule!=TRANSFER_RULE_WHITE_LIST_ONLY || addressWhiteList[_to]);

        //Verify if the ticket can be
        require(tickets[_tokenId].allowedTransfers!=0);
        
        //Decrease the allowed transfers number
        if(tickets[_tokenId].allowedTransfers>0)
            tickets[_tokenId].allowedTransfers--;
            
            
        // Reassign ownership, clear pending approvals, emit Transfer event.
        _transfer(msg.sender, _to, _tokenId);
   
    }

    /// @notice Grant another address the right to transfer a specific Ticket via
    ///  transferFrom(). This is the preferred flow for transfering NFTs to contracts.
    /// @param _to The address to be granted transfer approval. Pass address(0) to
    ///  clear all approvals.
    /// @param _tokenId The ID of the Ticket that can be transferred if this call succeeds.
    /// @dev Required for ERC-721 compliance.
    function approve(
        address _to,
        uint256 _tokenId
    )
        external
        whenNotPaused
    {
        // Only an owner can grant transfer approval.
        require(_owns(msg.sender, _tokenId));

        // Register the approval (replacing any previous approval).
        _approve(_tokenId, _to);

        // Emit approval event.
        emit Approval(msg.sender, _to, _tokenId);
    }

    /// @notice Transfer a Ticket owned by another address, for which the calling address
    ///  has previously been granted transfer approval by the owner.
    /// @param _from The address that owns the Ticket to be transfered.
    /// @param _to The address that should take ownership of the Ticket. Can be any address,
    ///  including the caller.
    /// @param _tokenId The ID of the Ticket to be transferred.
    /// @dev Required for ERC-721 compliance.
    function transferFrom(
        address _from,
        address _to,
        uint256 _tokenId
    )
        external
        whenNotPaused
    {
        // Safety check to prevent against an unexpected 0x0 default.
        require(_to != address(0));
        // Disallow transfers to this contract to prevent accidental misuse.
        // The contract should never own any Ticket (except very briefly
        // after a gen0 cat is created and before it goes on auction).
        require(_to != address(this));
        // Check for approval and valid ownership
        require(_approvedFor(msg.sender, _tokenId));
        require(_owns(_from, _tokenId));

        // Reassign ownership (also clears pending approvals and emits Transfer event).
        _transfer(_from, _to, _tokenId);
    }

    /// @notice Returns the total number of Tickets currently in existence.
    /// @dev Required for ERC-721 compliance.
    function totalSupply() public view returns (uint) {
        return tickets.length - 1;
    }

    /// @notice Returns the address currently assigned ownership of a given Ticket.
    /// @dev Required for ERC-721 compliance.
    function ownerOf(uint256 _tokenId)
        external
        view
        returns (address owner)
    {
        owner = ticketIndexToOwner[_tokenId];

        require(owner != address(0));
    }

    /// @notice Returns a list of all Ticket IDs assigned to an address.
    /// @param _owner The owner whose Ticket we are interested in.
    /// @dev This method MUST NEVER be called by smart contract code. First, it's fairly
    ///  expensive (it walks the entire Ticket array looking for Ticket belonging to owner),
    ///  but it also returns a dynamic array, which is only supported for web3 calls, and
    ///  not contract-to-contract calls.
    function tokensOfOwner(address _owner) external view returns(uint256[] ownerTokens) {
        uint256 tokenCount = balanceOf(_owner);

        if (tokenCount == 0) {
            // Return an empty array
            return new uint256[](0);
        } else {
            uint256[] memory result = new uint256[](tokenCount);
            uint256 totalTickets = totalSupply();
            uint256 resultIndex = 0;

            // We count on the fact that all ticket have IDs starting at 1 and increasing
            // sequentially up to the totalTicket count.
            uint256 ticketId;

            for (ticketId = 1; ticketId <= totalTickets; ticketId++) {
                if (ticketIndexToOwner[ticketId] == _owner) {
                    result[resultIndex] = ticketId;
                    resultIndex++;
                }
            }

            return result;
        }
    }

    /// @dev Adapted from memcpy() by @arachnid (Nick Johnson <arachnid@notdot.net>)
    ///  This method is licenced under the Apache License.
    ///  Ref: https://github.com/Arachnid/solidity-stringutils/blob/2f6ca9accb48ae14c66f1437ec50ed19a0616f78/strings.sol
    function _memcpy(uint _dest, uint _src, uint _len) private view {
        // Copy word-length chunks while possible
        for(; _len >= 32; _len -= 32) {
            assembly {
                mstore(_dest, mload(_src))
            }
            _dest += 32;
            _src += 32;
        }

        // Copy remaining bytes
        uint256 mask = 256 ** (32 - _len) - 1;
        assembly {
            let srcpart := and(mload(_src), not(mask))
            let destpart := and(mload(_dest), mask)
            mstore(_dest, or(destpart, srcpart))
        }
    }
    

    /// @dev Adapted from toString(slice) by @arachnid (Nick Johnson <arachnid@notdot.net>)
    ///  This method is licenced under the Apache License.
    ///  Ref: https://github.com/Arachnid/solidity-stringutils/blob/2f6ca9accb48ae14c66f1437ec50ed19a0616f78/strings.sol
    function _toString(bytes32[4] _rawBytes, uint256 _stringLength) private view returns (string) {
        string memory outputString = new string(_stringLength);
        uint256 outputPtr;
        uint256 bytesPtr;

        assembly {
            outputPtr := add(outputString, 32)
            bytesPtr := _rawBytes
        }

        _memcpy(outputPtr, bytesPtr, _stringLength);

        return outputString;
    }
    

    /// @notice Returns a URI pointing to a metadata package for this token conforming to
    ///  ERC-721 (https://github.com/ethereum/EIPs/issues/721)
    /// @param _tokenId The ID number of the Ticket whose metadata should be returned.
    function tokenMetadata(uint256 _tokenId, string _preferredTransport) external view returns (string infoUrl) {
        require(erc721Metadata != address(0));
        bytes32[4] memory buffer;
        uint256 count;
        (buffer, count) = erc721Metadata.getMetadata(_tokenId, _preferredTransport);

        return _toString(buffer, count);
    }
}

/// @title TicketPaymentGateway Core
/// @dev Contains models, variables, and internal methods for the order.
/// @notice We omit a fallback function to prevent accidental sends to this contract.
contract TicketPaymentGateway is Ownable{



    /// @dev The ERC-165 interface signature for ERC-721.
    ///  Ref: https://github.com/ethereum/EIPs/issues/165
    ///  Ref: https://github.com/ethereum/EIPs/issues/721
    bytes4 constant InterfaceSignature_ERC721 = bytes4(0x9a20483d);

    /// @dev Constructor creates a reference to the NFT ownership contract
    ///  and verifies the owner cut is in the valid range.
    /// @param _nftAddress - address of a deployed contract implementing
    ///  the Nonfungible Interface.
    /// @param _cut - percent cut the owner takes on each auction, must be
    ///  between 0-10,000.
    constructor(address _nftAddress) public payable {
        ERC721 candidateContract = ERC721(_nftAddress);
        require(candidateContract.supportsInterface(InterfaceSignature_ERC721));
        nonFungibleContract = candidateContract;
    }
    
   
   bool public isTicketPaymentGateway = true;
   
   
    Payment[] payments; 

    /*** CONSTANTS ***/
    uint8 constant STATE_PENDING = 0;
    uint8 constant STATE_COMPLETED = 1;
    uint8 constant STATE_REJECTED = 2;
    
    // Represents an auction on an NFT
    struct Payment {
        // Current owner of NFT
        address buyer;
        
        // Price (in wei)
        uint128 amount;
        
        // the block timestamp when the order was placed
        uint32 placedTime;        

        // the block timestamp when the order was processed
        uint32 processedTime; 
        
        //The status of the payment
        uint64 paymentState;
        
    }

    // Reference to contract tracking NFT ownership
    ERC721 public nonFungibleContract;


    // Mapping paymentUUID to 
    mapping (uint256 => uint256) public orderUUIDToPaymentId;

    event PaymentInserted(uint256 cartUUID, uint256 price, address buyer, uint256 newPaymentId);
    event PaymentSuccessfull(uint256 cartUUID, address buyer, uint256 paymentId);
    event PaymentRejected(uint256 cartUUID,  address buyer, uint256 paymentId);
    
    
    /// @notice this function is used to purchase the content of a shopping cart 
    /// @param _cartUUID The ID of the cart containig items
    function buy (
        uint256 _cartUUID
    )
        external
        payable
        returns (uint)
    {

        // Check if the order was already paied
        require(!(orderUUIDToPaymentId[_cartUUID]>0));
        
        Payment memory _payment = Payment({
            buyer: msg.sender,
            amount: uint128(msg.value),
            placedTime: uint32(now),
            processedTime: 0,
            paymentState: STATE_PENDING
        });

        //register the payment
        uint256 newPaymentId = payments.push(_payment) - 1;
        //map the cart id to payment id
        orderUUIDToPaymentId[_cartUUID] = newPaymentId;
        
        //Emits the payment inserted event
        emit PaymentInserted(_cartUUID, msg.value, msg.sender, newPaymentId);
        
        return newPaymentId;

    }



    /// @notice this function is used to confirm a payment
    /// @param _cartUUID The ID of the cart containig items
    /// @param _ticketUUID ticketUUID
    /// @param _organizationUUID organization uuid
    /// @param _priceCapGwai price cap
    /// @param _validFrom validity from 
    /// @param  _validUntil expiration date
    /// @param  _allowedTransfers number of allowed transfer
    /// @param  _transferRule rules applied for transferring title
    /// @param  _ticketState status of the ticket  
    function confirmPayment (
        uint256 _cartUUID,
        uint256 _ticketUUID,
        uint256 _organizationUUID,
        uint256 _priceCapGwai,
        uint256 _validFrom,
        uint256 _validUntil,
        uint256 _allowedTransfers,
        uint256 _transferRule,
        uint256 _ticketState )
        external
        onlyOwner
        returns (uint)
    {

        uint256 paymentId = orderUUIDToPaymentId[_cartUUID];
        
        //Only pending order are processed
        require(payments[paymentId]. paymentState == STATE_PENDING);
        
        //Verify the payment amount
        require(payments[paymentId].amount>0);
        
        //updates the 
        payments[paymentId].paymentState=STATE_COMPLETED;
        payments[paymentId].processedTime=uint32(now);
        
        /*nonFungibleContract.enrollTicket(_ticketUUID,
        _organizationUUID,
        _priceCapGwai,
        _validFrom,
        _validUntil,
        _allowedTransfers,
        _transferRule,
        _ticketState,
        payments[paymentId].buyer );*/
        

        //Emits the payment inserted event
        emit PaymentSuccessfull(_cartUUID, msg.sender, paymentId);
    }    
    

  /// @notice this function is used to reject a payment
    /// @param _cartUUID The ID of the cart containig items
    
    function rejectPayment (
        uint256 _cartUUID
    )
        external
        onlyOwner
        returns (uint)
    {

        uint256 paymentId = orderUUIDToPaymentId[_cartUUID];
        
        //Only pending order are processed
        require(payments[paymentId]. paymentState == STATE_PENDING);
        
        //Verify the payment amount
        require(payments[paymentId].amount>0);
        
        //updates the 
        payments[paymentId].paymentState=STATE_REJECTED;
        payments[paymentId].processedTime=uint32(now);

        //Emits the payment inserted event
        emit PaymentRejected(_cartUUID, msg.sender, paymentId);

    }    
    

}


/// @title Handles payment gateway for buy tickets 
contract TicketCoinPayable is TicketOwnership {

    // @notice The auction contract variables are defined in KittyBase to allow
    //  us to refer to them in KittyOwnership to prevent accidental transfers.
    // `saleAuction` refers to the auction for gen0 and p2p sale of kitties.
    // `siringAuction` refers to the auction for siring rights of kitties.

    /// @dev Sets the reference to the Ticket Payment Gateway.
    /// @param _address - Address of gateway contract.
    function setTicketPaymentGatewayAddress(address _address) external onlyCTO {
        TicketPaymentGateway candidateContract = TicketPaymentGateway(_address);

        // NOTE: verify that a contract is what we expect - https://github.com/Lunyr/crowdsale-contracts/blob/cfadd15986c30521d8ba7d5b6f57b4fefcc7ac38/contracts/LunyrToken.sol#L117
        require(candidateContract.isTicketPaymentGateway());

        // Set the new contract address
        ticketPaymentGateway = candidateContract;
    }
}



 /// @title The core contract to manage tickets
contract TicketManagement is TicketCoinPayable  {

    /// @notice this function is used to enroll a new token
    /// @param _ticketUUID ticketUUID
    /// @param _organizationUUID organization uuid
    /// @param _priceCapGwai price cap
    /// @param _validFrom validity from 
    /// @param  _validUntil expiration date
    /// @param  _allowedTransfers number of allowed transfer
    /// @param  _transferRule rules applied for transferring title
    /// @param  _ticketState status of the ticket
    /// @param  _owner  owber if the ticket
    function enrollTicket(
        uint256 _ticketUUID,
        uint256 _organizationUUID,
        uint256 _priceCapGwai,
        uint256 _validFrom,
        uint256 _validUntil,
        int256 _allowedTransfers,
        uint256 _transferRule,
        uint256 _ticketState,
        address _owner    )
        external
        whenNotPaused
        returns (uint)
    {
        
        //OnlyCTO o MarketPlace
        require(msg.sender == ctoAddress || msg.sender == address(ticketPaymentGateway));
        
        //checks the ticket existence
        require(ticketUUIDToIndex[_ticketUUID]==0);
        
        return 
        _createTicket(_ticketUUID,
        _organizationUUID,
        _priceCapGwai,
        _validFrom,
        _validUntil,
        _allowedTransfers,
        _transferRule,
        _ticketState,
        _owner );
    }
        
        

    /// @notice this function is used to cancel a token when it should't be usable
    /// @param _tokenId The ID of the Ticket to transfer.
    function cancel(
        uint256 _tokenId
    )
        external
        whenNotPaused
    {
 
        // Only CTO and organizator can consume ticket
        require(msg.sender == ctoAddress || uint128(addressToOrganizationUUID[msg.sender]) == tickets[_tokenId].organizationUUID);
        
        
        // cancel the token.
        _cancel(_tokenId);
    }
    

    /// @notice this function is used to consume a token when it should't be usable
    /// @param _tokenId The ID of the Ticket to transfer.
    function consume(
        uint256 _tokenId
    )
        external
        whenNotPaused
    {

        // Only CTO and organizator can consume ticket
        require(msg.sender == ctoAddress || uint128(addressToOrganizationUUID[msg.sender]) == tickets[_tokenId].organizationUUID);

        // cancel the token.
        _consume(_tokenId);
    }
    
    
          
      
    /// @notice this function is used to authorize an address to conusme a ticket
    /// @param addressToGrant the address to grant.
    /// @param organizationUUID the organizationUUID to grant device to.
    function authorize(
        address addressToGrant,
        uint256 organizationUUID
    )
        external
        onlyCTO
        whenNotPaused
    {
        addressToOrganizationUUID[addressToGrant]=organizationUUID;
    }


   /// @notice this function is used to revoke an address to conusme a ticket
    /// @param addressToRevoke The address to revoke
    function revoke(
        address addressToRevoke
    )
        external
        onlyCTO
        whenNotPaused
    {
        delete addressToOrganizationUUID[addressToRevoke];
    }
           
                            

 /// @notice this function is used to authorize an address to conusme a ticket
    /// @param _address the address to insert in white list.
    function insertInWhiteList(
        address _address
    )
        external
        onlyCTO
        whenNotPaused
    {
        addressWhiteList[_address]=true;
    }


    function removeFromWhiteList(
        address _address
    )
        external
        onlyCTO
        whenNotPaused
    {
        delete addressWhiteList[_address];
    }
        
   
}





/// @title TicketCoinCore: TicketCoin tickets in blockchain.
/// @dev The main TicketCoin contract, keeps track of tickets emitted by ticketcoin.io
contract TicketCoinCore is TicketManagement {

   
    // Set in case the core contract is broken and an upgrade is required
    address public newContractAddress;

    /// @notice Creates the main TicketCoin smart contract instance.
    constructor() public payable{
        // Starts paused.
        paused = true;

        // the creator of the contract is the initial CEO
        ceoAddress = msg.sender;

        // the creator of the contract is also the initial COO
        ctoAddress = msg.sender;        


    }

    /// @dev Used to mark the smart contract as upgraded, in case there is a serious
    ///  breaking bug. This method does nothing but keep track of the new contract and
    ///  emit a message indicating that the new address is set. It's up to clients of this
    ///  contract to update to the new contract address in that case. (This contract will
    ///  be paused indefinitely if such an upgrade takes place.)
    /// @param _v2Address new address
    function setNewAddress(address _v2Address) external onlyCEO whenPaused {
        // See README.md for updgrade plan
        newContractAddress = _v2Address;
        emit ContractUpgrade(_v2Address);
    }

    /// @notice No tipping!
    /// @dev Reject all Ether from being sent here, unless it's from the
    /// ticketMarketPlace contract
    function() external payable {
        require(
            msg.sender == address(ticketPaymentGateway) 
        );
    }

    /// @notice Returns all the relevant information about a specific kitty.
    /// @param _id The ID of the kitty of interest.
    function getTicket(uint256 _id)
        external
        view
        returns (
        bool isValid,
        uint256 ticketUUID,
        uint256 organizationUUID,
        uint256 priceCapGwai,
        uint256 enrollTime,
        uint256 validFrom,
        uint256 validUntil,
        uint256 consumedTime,
        uint256 canceledTime,
        int256 allowedTransfers,
        uint256 ticketState,
        uint256 transferRule
    ) {
        
        
        Ticket storage ticket = tickets[_id];

        // show if the ticket s valid
        isValid = (ticket.ticketState == STATE_VALID);
    
        ticketUUID = uint256(ticket.ticketUUID);
        organizationUUID = uint256(ticket.organizationUUID);
        priceCapGwai = uint256(ticket.priceCapGwai);
        enrollTime = uint256(ticket.enrollTime);
        validFrom = uint256(ticket.validFrom);
        validUntil = uint256(ticket.validUntil);
        consumedTime = uint256(ticket.consumedTime);
        canceledTime = uint256(ticket.canceledTime);
        allowedTransfers = int256(ticket.allowedTransfers);
        ticketState = uint256(ticket.ticketState);
        transferRule = uint256(ticket.transferRule);
     
    }

    /// @dev Override unpause so it requires all external contract addresses
    ///  to be set before contract can be unpaused. Also, we can't have
    ///  newContractAddress set either, because then the contract was upgraded.
    /// @notice This is public rather than external so we can call super.unpause
    ///  without using an expensive CALL.
    function unpause() public onlyCEO whenPaused {
        require(newContractAddress == address(0));

        // Actually unpause the contract.
        super.unpause();
    }

    // @dev Allows the CFO to capture the balance available to the contract.
    function withdrawBalance() external onlyCFO {
        uint256 balance = address(this).balance;
        cfoAddress.transfer(balance);
    }
}