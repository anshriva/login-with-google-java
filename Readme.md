## Authentication with Google.  
Login with Google is used for password less authentication. 
It does not manage sessions and authorization. 
For session and authorizations, we have different flow like oauth2. This is specifically for password less auth only. 

### Useful links: 
1. [Google doc with fundamentals](https://developers.google.com/identity/gsi/web/guides/overview)
2. [Google cloud Credentials page](https://console.cloud.google.com/apis/credentials)

### Login options
1. Redirection flow
    #### Architecture 
    ![Diagram](readme-resources/with-redirection.png)
2. Single page flow
    #### Architecture
    ![Diagram](readme-resources/without-redirection.png)