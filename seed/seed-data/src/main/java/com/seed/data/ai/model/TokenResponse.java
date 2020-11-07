package com.seed.data.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 23:59
 */
@Data
@NoArgsConstructor
public class TokenResponse {

    /**
     * refresh_token : 25.b55fe1d287227ca97aab219bb249b8ab.315360000.1798284651.282335-8574074
     * expires_in : 2592000
     * scope : public wise_adapt
     * session_key : 9mzdDZXu3dENdFZQurfg0Vz8slgSgvvOAUebNFzyzcpQ5EnbxbF+hfG9DQkpUVQdh4p6HbQcAiz5RmuBAja1JJGgIdJI
     * access_token : 24.6c5e1ff107f0e8bcef8c46d3424a0e78.2592000.1485516651.282335-8574074
     * session_secret : dfac94a3489fe9fca7c3221cbf7525ff
     */

    private String refresh_token;
    private int expires_in;
    private String scope;
    private String session_key;
    private String access_token;
    private String session_secret;

    /**
     * "error": "invalid_client",
     * "error_description": "unknown client id"
     */
    private String error;
    private String error_description;

}
