package openway.service;

public interface UFXService {

    /**
     * @param sName surname
     * @param name name
     * @param clientNumber client id from db,
     *                     must be unique
     * @param regNumberApp application id,
     *                  must be unique
     *
     * clientNumber == regNumber in this function, but this is not the same fields in general
     *
     * @return string with generated request
     */
    String RequestCreateClient(String sName, String name,
                               String clientNumber, String regNumberApp);


    /**
     *
     * @param clientNumber client id just created in CreateClientRequest, must be the same
     * @param regNumberClient client reg number, also from createClientRequest, must be the same
     * @param regNumberApp application reg number, must be unique
     *
     * clientNumber == regNumberClient != regNumberApp
     * @return string with generated request
     */
    String RequestCreateIssContract(String clientNumber,
                                    String regNumberClient,
                                    String regNumberApp,
                                    String contractNumber);

    String RequestCreateAcqContract();

    /**
     *
     * @param url destination address
     * @param request request for send
     * @return string with result
     */
    String SendRequest(String url, String request);

    /**
     *
     * @param data data for generation
     * @return string with generated id for client and etc
     */
    String GenerateId(String data);

}

