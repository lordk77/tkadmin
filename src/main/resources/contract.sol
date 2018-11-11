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
  function Ownable() {
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
  function transferOwnership(address newOwner) onlyOwner {
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
     function name() public view returns (string name);
     function symbol() public view returns (string symbol);
     function tokensOfOwner(address _owner) external view returns (uint256[] tokenIds);
     function tokenMetadata(uint256 _tokenId, string _preferredTransport) public view returns (string infoUrl);

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
    //     - The Organization: The Organization can mint ticket.
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
    address public organizationAddress;

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

    /// @dev Access modifier for Organization-only functionality
    modifier onlyOrganization() {
        require(msg.sender == organizationAddress);
        _;
    }

    modifier onlyCLevel() {
        require(
            msg.sender == organizationAddress ||
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

    /// @dev Assigns a new address to act as the Organization. Only available to the current CEO.
    /// @param _newOrganization The address of the new Organization
    function setOrganization(address _newOrganization) external onlyCEO {
        require(_newOrganization != address(0));

        organizationAddress = _newOrganization;
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
    event Consume(address owner, uint256 ticketId);

    /// @dev The Birth event is fired whenever a ticket is consumed bat the event gate.
    event Cancel(address owner, uint256 ticketId);


    /*** DATA TYPES ***/

    /// @dev The main Ticket struct. Every ticket in Ticketcoin is represented by a copy
    ///  of this structure, so great care was taken to ensure that it fits neatly into
    ///  exactly two 256-bit words. Note that the order of the members in this structure
    ///  is important because of the byte-packing rules used by Ethereum.
    ///  Ref: http://solidity.readthedocs.io/en/develop/miscellaneous.html
    struct Ticket {
        // The Ticket unique identifier.
        uint256 ticketUUID;

        // The timestamp from the block when this ticket was enrolled.
        uint64 enrollTime;
        
       // The timestamp from the block when this ticket was cancealed.
        uint64 canceledTime;

       // The timestamp from the block when this ticket was consumed.
        uint64 consumedTime;
        
       // The timestamp from the block when this ticket was spent.
        uint32 eventID;
        
       // The id of the organization
        uint16 organizationID;
        
        
       //The number of allowed transfers.
       uint8 transferAllowed;
        
       // The timestamp from the block when this ticket was spent.
       uint8 transferDone;


    }

    /*** CONSTANTS ***/


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
        Transfer(_from, _to, _tokenId);
    }

    /// @dev Consume the validity of a ticket.
    function _consume(uint256 _tokenId) internal {
        // consume the ticket
        ticketIndexToOwner[_tokenId].consumedTime=uint64(now);
        
        // Emit the transfer event.
        Consume(_tokenId);
    }

    /// @dev Cancel the ticket
    function _cancel(uint256 _tokenId) internal {
        // cancel the ticket
        ticketIndexToOwner[_tokenId].canceledTime=uint64(now);

        // Emit the transfer event.
        Cancel(_tokenId);
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
        uint256 _organizationID,
        uint256 _eventID,
        uint256 _transferAllowed,
        address _owner
    )
        internal
        returns (uint)
    {
        // These requires are not strictly necessary, our calling code should make
        // sure that these conditions are never broken. 
        
        require(_organizationID == uint256(uint16(_organizationID)));
        require(_eventID == uint256(uint16(_eventID)));
        require(_transferAllowed == uint256(uint8(_transferAllowed)));

        Ticket memory _ticket = Ticket({
            ticketUUID: _ticketUUID,
            enrollTime: uint64(now),
            canceledTime: 0,
            consumedTime: 0,
            eventID: uint32(_eventID),
            transferAllowed: uint8(_transferAllowed),
            transferDone: 0
        });
        
        
        
        
        uint256 newTicketId = tickets.push(_ticket) - 1;

 		//avoid overflow
        require(newTicketId == uint256(uint32(newTicketId)));



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
        if (_tokenId == 1) {
            buffer[0] = "Hello World! :D";
            count = 15;
        } else if (_tokenId == 2) {
            buffer[0] = "I would definitely choose a medi";
            buffer[1] = "um length string.";
            count = 49;
        } else if (_tokenId == 3) {
            buffer[0] = "Lorem ipsum dolor sit amet, mi e";
            buffer[1] = "st accumsan dapibus augue lorem,";
            buffer[2] = " tristique vestibulum id, libero";
            buffer[3] = " suscipit varius sapien aliquam.";
            count = 128;
        }
    }
}


/// @title The facet of the TicketCoin core contract that manages ownership, ERC-721 (draft) compliant.
/// @dev Ref: https://github.com/ethereum/EIPs/issues/721
contract TicketOwnership is TicketBase, ERC721 {

    /// @notice Name and symbol of the non fungible token, as defined in ERC721.
    string public constant name = "TicketCoinTicket";
    string public constant symbol = "TCKT";

    // The contract that will return kitty metadata
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
    /// @param _tokenId kitten id, only valid when > 0
    function _owns(address _claimant, uint256 _tokenId) internal view returns (bool) {
        return ticketIndexToOwner[_tokenId] == _claimant;
    }

    /// @dev Checks if a given address currently has transferApproval for a particular Ticket.
    /// @param _claimant the address we are confirming kitten is approved for.
    /// @param _tokenId kitten id, only valid when > 0
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
        // The contract should never own any kitties (except very briefly
        // after a gen0 cat is created and before it goes on auction).
        require(_to != address(this));


        // You can only send your own Ticket.
        require(_owns(msg.sender, _tokenId));

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
        Approval(msg.sender, _to, _tokenId);
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
        var outputString = new string(_stringLength);
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
    /// @param _tokenId The ID number of the Kitty whose metadata should be returned.
    function tokenMetadata(uint256 _tokenId, string _preferredTransport) external view returns (string infoUrl) {
        require(erc721Metadata != address(0));
        bytes32[4] memory buffer;
        uint256 count;
        (buffer, count) = erc721Metadata.getMetadata(_tokenId, _preferredTransport);

        return _toString(buffer, count);
    }
}











/// @title all functions related to creating kittens
contract KittyMinting is KittyAuction {

    // Limits the number of cats the contract owner can ever create.
    uint256 public constant PROMO_CREATION_LIMIT = 5000;
    uint256 public constant GEN0_CREATION_LIMIT = 45000;

    // Constants for gen0 auctions.
    uint256 public constant GEN0_STARTING_PRICE = 10 finney;
    uint256 public constant GEN0_AUCTION_DURATION = 1 days;

    // Counts the number of cats the contract owner has created.
    uint256 public promoCreatedCount;
    uint256 public gen0CreatedCount;

    /// @dev we can create promo kittens, up to a limit. Only callable by COO
    /// @param _genes the encoded genes of the kitten to be created, any value is accepted
    /// @param _owner the future owner of the created kittens. Default to contract COO
    function createPromoKitty(uint256 _genes, address _owner) external onlyCOO {
        address kittyOwner = _owner;
        if (kittyOwner == address(0)) {
             kittyOwner = cooAddress;
        }
        require(promoCreatedCount < PROMO_CREATION_LIMIT);

        promoCreatedCount++;
        _createKitty(0, 0, 0, _genes, kittyOwner);
    }

    /// @dev Creates a new gen0 kitty with the given genes and
    ///  creates an auction for it.
    function createGen0Auction(uint256 _genes) external onlyCOO {
        require(gen0CreatedCount < GEN0_CREATION_LIMIT);

        uint256 kittyId = _createKitty(0, 0, 0, _genes, address(this));
        _approve(kittyId, saleAuction);

        saleAuction.createAuction(
            kittyId,
            _computeNextGen0Price(),
            0,
            GEN0_AUCTION_DURATION,
            address(this)
        );

        gen0CreatedCount++;
    }

    /// @dev Computes the next gen0 auction starting price, given
    ///  the average of the past 5 prices + 50%.
    function _computeNextGen0Price() internal view returns (uint256) {
        uint256 avePrice = saleAuction.averageGen0SalePrice();

        // Sanity check to ensure we don't overflow arithmetic
        require(avePrice == uint256(uint128(avePrice)));

        uint256 nextPrice = avePrice + (avePrice / 2);

        // We never auction for less than starting price
        if (nextPrice < GEN0_STARTING_PRICE) {
            nextPrice = GEN0_STARTING_PRICE;
        }

        return nextPrice;
    }
}


/// @title CryptoKitties: Collectible, breedable, and oh-so-adorable cats on the Ethereum blockchain.
/// @author Axiom Zen (https://www.axiomzen.co)
/// @dev The main CryptoKitties contract, keeps track of kittens so they don't wander around and get lost.
contract KittyCore is KittyMinting {

    // This is the main CryptoKitties contract. In order to keep our code seperated into logical sections,
    // we've broken it up in two ways. First, we have several seperately-instantiated sibling contracts
    // that handle auctions and our super-top-secret genetic combination algorithm. The auctions are
    // seperate since their logic is somewhat complex and there's always a risk of subtle bugs. By keeping
    // them in their own contracts, we can upgrade them without disrupting the main contract that tracks
    // kitty ownership. The genetic combination algorithm is kept seperate so we can open-source all of
    // the rest of our code without making it _too_ easy for folks to figure out how the genetics work.
    // Don't worry, I'm sure someone will reverse engineer it soon enough!
    //
    // Secondly, we break the core contract into multiple files using inheritence, one for each major
    // facet of functionality of CK. This allows us to keep related code bundled together while still
    // avoiding a single giant file with everything in it. The breakdown is as follows:
    //
    //      - TicketBase: This is where we define the most fundamental code shared throughout the core
    //             functionality. This includes our main data storage, constants and data types, plus
    //             internal functions for managing these items.
    //
    //      - TicketCoinAccessControl: This contract manages the various addresses and constraints for operations
    //             that can be executed only by specific roles. Namely CEO, CFO and Organization.
    //
    //      - TicketOwnership: This provides the methods required for basic non-fungible token
    //             transactions, following the draft ERC-721 spec (https://github.com/ethereum/EIPs/issues/721).
    //
    //
    //      - KittyMinting: This final facet contains the functionality we use for creating new gen0 cats.
    //             We can make up to 5000 "promo" cats that can be given away (especially important when
    //             the community is new), and all others can only be created and then immediately put up
    //             for auction via an algorithmically determined starting price. Regardless of how they
    //             are created, there is a hard limit of 50k gen0 cats. After that, it's all up to the
    //             community to breed, breed, breed!

    // Set in case the core contract is broken and an upgrade is required
    address public newContractAddress;

    /// @notice Creates the main CryptoKitties smart contract instance.
    function KittyCore() public {
        // Starts paused.
        paused = true;

        // the creator of the contract is the initial CEO
        ceoAddress = msg.sender;

        // the creator of the contract is also the initial COO
        cooAddress = msg.sender;

        // start with the mythical kitten 0 - so we don't have generation-0 parent issues
        _createKitty(0, 0, 0, uint256(-1), address(0));
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
        ContractUpgrade(_v2Address);
    }

    /// @notice No tipping!
    /// @dev Reject all Ether from being sent here, unless it's from one of the
    ///  two auction contracts. (Hopefully, we can prevent user accidents.)
    function() external payable {
        require(
            msg.sender == address(saleAuction) ||
            msg.sender == address(siringAuction)
        );
    }

    /// @notice Returns all the relevant information about a specific kitty.
    /// @param _id The ID of the kitty of interest.
    function getKitty(uint256 _id)
        external
        view
        returns (
        bool isGestating,
        bool isReady,
        uint256 cooldownIndex,
        uint256 nextActionAt,
        uint256 siringWithId,
        uint256 birthTime,
        uint256 matronId,
        uint256 sireId,
        uint256 generation,
        uint256 genes
    ) {
        Kitty storage kit = kitties[_id];

        // if this variable is 0 then it's not gestating
        isGestating = (kit.siringWithId != 0);
        isReady = (kit.cooldownEndBlock <= block.number);
        cooldownIndex = uint256(kit.cooldownIndex);
        nextActionAt = uint256(kit.cooldownEndBlock);
        siringWithId = uint256(kit.siringWithId);
        birthTime = uint256(kit.birthTime);
        matronId = uint256(kit.matronId);
        sireId = uint256(kit.sireId);
        generation = uint256(kit.generation);
        genes = kit.genes;
    }

    /// @dev Override unpause so it requires all external contract addresses
    ///  to be set before contract can be unpaused. Also, we can't have
    ///  newContractAddress set either, because then the contract was upgraded.
    /// @notice This is public rather than external so we can call super.unpause
    ///  without using an expensive CALL.
    function unpause() public onlyCEO whenPaused {
        require(saleAuction != address(0));
        require(siringAuction != address(0));
        require(geneScience != address(0));
        require(newContractAddress == address(0));

        // Actually unpause the contract.
        super.unpause();
    }

    // @dev Allows the CFO to capture the balance available to the contract.
    function withdrawBalance() external onlyCFO {
        uint256 balance = this.balance;
        // Subtract all the currently pregnant kittens we have, plus 1 of margin.
        uint256 subtractFees = (pregnantKitties + 1) * autoBirthFee;

        if (balance > subtractFees) {
            cfoAddress.send(balance - subtractFees);
        }
    }
}