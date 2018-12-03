pragma solidity ^0.4.11;


/// @title Base contract for Tickets. Holds all common structs, events and base variables.
contract TicketBase 
{
    /// @dev The main Ticket struct. Every ticket in Ticketcoin is represented by a copy
    ///  of this structure, so great care was taken to ensure that it fits neatly into
    ///  exactly two 256-bit words. Note that the order of the members in this structure
    ///  is important because of the byte-packing rules used by Ethereum.
    ///  Ref: http://solidity.readthedocs.io/en/develop/miscellaneous.html
    struct Ticket {
        
        //keccak256 ticketUUID + msg.sender
        bytes32 owerHash;
        
        
        // The Ticket unique identifier.
        uint32 ticketUUID;
        
        // The id of the organization that enrolled/owns the ticket
        uint32 organizationUUID;


        // priceCapGwai.
        uint32 priceCapGwai;
        
        
        // The timestamp from the block when this ticket was enrolled.
        uint32 enrollTime;

         // The date since whom the ticket can be spent.
        uint32 validFrom;       
        
         // The expiration date of the ticket.
        uint32 validUntil;        
        
       // The timestamp from the block when this ticket was consumed
        uint32 stateChangedTime;

        //The number remainig transfers allowed.  -1 means infinite transfers      
        int16 allowedTransfers;
        
        //The status of the ticket
        uint8 ticketState;
        
        //The transfer rule to apply
        uint8 transferRule;
        

        
    }
    
    Ticket[] tickets;

    // @dev A mapping from ticketUUID to ticketIndex
    mapping (uint256 => uint256) ticketUUIDToIndex;  

    /// @dev A mapping from ticket IDs to the address that owns them. 
    mapping (uint256 => address) public ticketIndexToOwner;
    
    
    
    function createTicket(uint256 _ticketUUID, bytes32 _hash) public  {
    
        //creates the ticket structure
        Ticket memory _ticket = Ticket({
            owerHash:_hash,
            ticketUUID: uint32(_ticketUUID),
            organizationUUID: uint32(0),
            priceCapGwai: uint32(0),
            enrollTime: uint32(now),
            validFrom: uint32(0),
            validUntil: uint32(0),
            stateChangedTime: 0,
            allowedTransfers: int16(0),
            ticketState: uint8(0),
            transferRule: uint8(0)
        });
        

        uint256 newTicketId = tickets.push(_ticket) - 1;

 		
        //link the ticketID to ticketUUID
        ticketUUIDToIndex[_ticketUUID]=newTicketId;

        ticketIndexToOwner[newTicketId]=msg.sender;


        
    }
    
    
    function getHash(uint256 ticketUUID) public view returns (bytes32 hash) {
    
       return keccak256(abi.encodePacked(ticketUUID, msg.sender));
    }
    
    function _owns(uint256 _tokenId, address _address) internal view returns (bool ownerOwns) {
    
        return  keccak256(abi.encodePacked(bytes32(tickets[_tokenId].ticketUUID), _address)) == tickets[_tokenId].owerHash;
    }
    
    
     function claim(uint256 _tokenId) public  {
    
        require(_owns(_tokenId, msg.sender));
        
        ticketIndexToOwner[_tokenId]=msg.sender;
    }
    
    
    
    
    
}