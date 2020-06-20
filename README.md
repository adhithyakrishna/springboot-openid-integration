# springboot-openid-integration
Open id integration

This project provides the boiler plate code for anyone who wants to use google open id for authorisation and any of the other google apis, include scope, client id and client secret in  **OAuth2LoginConfig** file.

This code automatically fetches the access token, refreshes the token if the old token expires and can be Autowired to **Oauth2TokenService** to be used across the project. 
