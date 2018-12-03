package io.ticketcoin.web3j.wrapper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.StaticArray4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.0.1.
 */
public class ERC721Metadata extends Contract {
    private static final String BINARY = "{\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"60806040526040516020806105ae8339810180604052810190808051906020019092919050505060008190508073ffffffffffffffffffffffffffffffffffffffff166301ffc9a7639a20483d7c0100000000000000000000000000000000000000000000000000000000026040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19168152602001915050602060405180830381600087803b1580156100fb57600080fd5b505af115801561010f573d6000803e3d6000fd5b505050506040513d602081101561012557600080fd5b8101908080519060200190929190505050151561014157600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505061041c806101926000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806394e8767d1461005c578063cb4799f2146100a5578063dd1b7a0f1461015b575b600080fd5b34801561006857600080fd5b50610087600480360381019080803590602001909291905050506101b2565b60405180826000191660001916815260200191505060405180910390f35b3480156100b157600080fd5b5061011660048036038101908080359060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061025b565b6040518083600460200280838360005b83811015610141578082015181840152602081019050610126565b505050509050018281526020019250505060405180910390f35b34801561016757600080fd5b506101706103a8565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000808214156101e4577f30000000000000000000000000000000000000000000000000000000000000009050610253565b5b60008211156102525761010081600190048115156101ff57fe5b0460010290507f01000000000000000000000000000000000000000000000000000000000000006030600a8481151561023457fe5b06010260010281179050600a8281151561024a57fe5b0491506101e5565b5b809050919050565b6102636103cd565b60007f687474703a2f2f7469636b6574636f696e2e696f2f7075626c69632f74636b2f82600060048110151561029557fe5b6020020190600019169081600019168152505061037b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633e878d52866040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561033b57600080fd5b505af115801561034f573d6000803e3d6000fd5b505050506040513d602081101561036557600080fd5b81019080805190602001909291905050506101b2565b82600160048110151561038a57fe5b60200201906000191690816000191681525050604090509250929050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6080604051908101604052806004906020820280388339808201915050905050905600a165627a7a723058207f695bf40e4d950012d548b1f7c1c6d7c120c177d7360d0e81620a463e1ac30f0029\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x40 MLOAD PUSH1 0x20 DUP1 PUSH2 0x5AE DUP4 CODECOPY DUP2 ADD DUP1 PUSH1 0x40 MSTORE DUP2 ADD SWAP1 DUP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH1 0x0 DUP2 SWAP1 POP DUP1 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH4 0x1FFC9A7 PUSH4 0x9A20483D PUSH29 0x100000000000000000000000000000000000000000000000000000000 MUL PUSH1 0x40 MLOAD DUP3 PUSH4 0xFFFFFFFF AND PUSH29 0x100000000000000000000000000000000000000000000000000000000 MUL DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP3 PUSH28 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF NOT AND PUSH28 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x20 PUSH1 0x40 MLOAD DUP1 DUP4 SUB DUP2 PUSH1 0x0 DUP8 DUP1 EXTCODESIZE ISZERO DUP1 ISZERO PUSH2 0xFB JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP GAS CALL ISZERO DUP1 ISZERO PUSH2 0x10F JUMPI RETURNDATASIZE PUSH1 0x0 DUP1 RETURNDATACOPY RETURNDATASIZE PUSH1 0x0 REVERT JUMPDEST POP POP POP POP PUSH1 0x40 MLOAD RETURNDATASIZE PUSH1 0x20 DUP2 LT ISZERO PUSH2 0x125 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 ADD SWAP1 DUP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP ISZERO ISZERO PUSH2 0x141 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP1 PUSH1 0x0 DUP1 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP POP POP PUSH2 0x41C DUP1 PUSH2 0x192 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN STOP PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH2 0x57 JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x94E8767D EQ PUSH2 0x5C JUMPI DUP1 PUSH4 0xCB4799F2 EQ PUSH2 0xA5 JUMPI DUP1 PUSH4 0xDD1B7A0F EQ PUSH2 0x15B JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x68 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x87 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x1B2 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH1 0x0 NOT AND PUSH1 0x0 NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0xB1 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x116 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP3 ADD DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP4 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP4 DUP4 DUP1 DUP3 DUP5 CALLDATACOPY DUP3 ADD SWAP2 POP POP POP POP POP POP SWAP2 SWAP3 SWAP2 SWAP3 SWAP1 POP POP POP PUSH2 0x25B JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP4 PUSH1 0x4 PUSH1 0x20 MUL DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0x141 JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x126 JUMP JUMPDEST POP POP POP POP SWAP1 POP ADD DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x167 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x170 PUSH2 0x3A8 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x0 DUP1 DUP3 EQ ISZERO PUSH2 0x1E4 JUMPI PUSH32 0x3000000000000000000000000000000000000000000000000000000000000000 SWAP1 POP PUSH2 0x253 JUMP JUMPDEST JUMPDEST PUSH1 0x0 DUP3 GT ISZERO PUSH2 0x252 JUMPI PUSH2 0x100 DUP2 PUSH1 0x1 SWAP1 DIV DUP2 ISZERO ISZERO PUSH2 0x1FF JUMPI INVALID JUMPDEST DIV PUSH1 0x1 MUL SWAP1 POP PUSH32 0x100000000000000000000000000000000000000000000000000000000000000 PUSH1 0x30 PUSH1 0xA DUP5 DUP2 ISZERO ISZERO PUSH2 0x234 JUMPI INVALID JUMPDEST MOD ADD MUL PUSH1 0x1 MUL DUP2 OR SWAP1 POP PUSH1 0xA DUP3 DUP2 ISZERO ISZERO PUSH2 0x24A JUMPI INVALID JUMPDEST DIV SWAP2 POP PUSH2 0x1E5 JUMP JUMPDEST JUMPDEST DUP1 SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH2 0x263 PUSH2 0x3CD JUMP JUMPDEST PUSH1 0x0 PUSH32 0x687474703A2F2F7469636B6574636F696E2E696F2F7075626C69632F74636B2F DUP3 PUSH1 0x0 PUSH1 0x4 DUP2 LT ISZERO ISZERO PUSH2 0x295 JUMPI INVALID JUMPDEST PUSH1 0x20 MUL ADD SWAP1 PUSH1 0x0 NOT AND SWAP1 DUP2 PUSH1 0x0 NOT AND DUP2 MSTORE POP POP PUSH2 0x37B PUSH1 0x0 DUP1 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH4 0x3E878D52 DUP7 PUSH1 0x40 MLOAD DUP3 PUSH4 0xFFFFFFFF AND PUSH29 0x100000000000000000000000000000000000000000000000000000000 MUL DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x20 PUSH1 0x40 MLOAD DUP1 DUP4 SUB DUP2 PUSH1 0x0 DUP8 DUP1 EXTCODESIZE ISZERO DUP1 ISZERO PUSH2 0x33B JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP GAS CALL ISZERO DUP1 ISZERO PUSH2 0x34F JUMPI RETURNDATASIZE PUSH1 0x0 DUP1 RETURNDATACOPY RETURNDATASIZE PUSH1 0x0 REVERT JUMPDEST POP POP POP POP PUSH1 0x40 MLOAD RETURNDATASIZE PUSH1 0x20 DUP2 LT ISZERO PUSH2 0x365 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 ADD SWAP1 DUP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x1B2 JUMP JUMPDEST DUP3 PUSH1 0x1 PUSH1 0x4 DUP2 LT ISZERO ISZERO PUSH2 0x38A JUMPI INVALID JUMPDEST PUSH1 0x20 MUL ADD SWAP1 PUSH1 0x0 NOT AND SWAP1 DUP2 PUSH1 0x0 NOT AND DUP2 MSTORE POP POP PUSH1 0x40 SWAP1 POP SWAP3 POP SWAP3 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP1 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 JUMP JUMPDEST PUSH1 0x80 PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 PUSH1 0x4 SWAP1 PUSH1 0x20 DUP3 MUL DUP1 CODESIZE DUP4 CODECOPY DUP1 DUP3 ADD SWAP2 POP POP SWAP1 POP POP SWAP1 JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 PUSH32 0x695BF40E4D950012D548B1F7C1C6D7C120C177D7360D0E81620A463E1AC30F00 0x29 \",\r\n"
            + "\t\"sourceMap\": \"14868:1567:0:-;;;15157:261;;;;;;;;;;;;;;;;;;;;;;;;;;;;15216:32;15266:11;15216:62;;15297:17;:35;;;15133:10;15126:18;;15297:62;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;8:9:-1;5:2;;;30:1;27;20:12;5:2;15297:62:0;;;;8:9:-1;5:2;;;45:16;42:1;39;24:38;77:16;74:1;67:27;5:2;15297:62:0;;;;;;;13:2:-1;8:3;5:11;2:2;;;29:1;26;19:12;2:2;15297:62:0;;;;;;;;;;;;;;;;15289:71;;;;;;;;15393:17;15371:19;;:39;;;;;;;;;;;;;;;;;;15157:261;;14868:1567;;;;;;\"\r\n"
            + "}";

    public static final String FUNC_UINTTOBYTES = "uintToBytes";

    public static final String FUNC_GETMETADATA = "getMetadata";

    public static final String FUNC_NONFUNGIBLECONTRACT = "nonFungibleContract";

    @Deprecated
    protected ERC721Metadata(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ERC721Metadata(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ERC721Metadata(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ERC721Metadata(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<byte[]> uintToBytes(BigInteger v) {
        final Function function = new Function(FUNC_UINTTOBYTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(v)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<Tuple2<List<byte[]>, BigInteger>> getMetadata(BigInteger _tokenId, String param1) {
        final Function function = new Function(FUNC_GETMETADATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId), 
                new org.web3j.abi.datatypes.Utf8String(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray4<Bytes32>>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<List<byte[]>, BigInteger>>(
                new Callable<Tuple2<List<byte[]>, BigInteger>>() {
                    @Override
                    public Tuple2<List<byte[]>, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<byte[]>, BigInteger>(
                                convertToNative((List<Bytes32>) results.get(0).getValue()), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<String> nonFungibleContract() {
        final Function function = new Function(FUNC_NONFUNGIBLECONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static ERC721Metadata load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Metadata(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ERC721Metadata load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Metadata(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ERC721Metadata load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ERC721Metadata load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC721Metadata> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger initialWeiValue, String _nftAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nftAddress)));
        return deployRemoteCall(ERC721Metadata.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<ERC721Metadata> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialWeiValue, String _nftAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nftAddress)));
        return deployRemoteCall(ERC721Metadata.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor, initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<ERC721Metadata> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String _nftAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nftAddress)));
        return deployRemoteCall(ERC721Metadata.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<ERC721Metadata> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String _nftAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nftAddress)));
        return deployRemoteCall(ERC721Metadata.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }
}
