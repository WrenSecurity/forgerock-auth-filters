/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2014-2015 ForgeRock AS.
 */

package org.forgerock.authz.modules.oauth2.http;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.parsing.Parser;

import org.forgerock.authz.AuthzTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(testName = "OAuth2Http")
public class OAuth2HttpAuthorizationModuleTestCases extends AuthzTestCase {

    @Test
    public void notAllowedWhenNoAccessTokenHeaderSet() {

        given().
            expect().
                statusCode(403).
                body("code", equalTo(403)).
                body("reason", equalTo("Forbidden")).
                body("message", equalTo("Access Token is null.")).
            when().
                get("/modules/oauth2/http/resource");
    }

    @Test
    public void allowedWhenAccessTokenHeaderSet() {

        given().
                header("Authorization", "Bearer VALID").
            expect().
                statusCode(200).
            when().
                get("/modules/oauth2/http/resource");
    }

    @Test
    public void notAllowedWhenAccessTokenHeaderIsNotBearer() {

        given().
                header("Authorization", "NotBearer VALID").
            expect().
                statusCode(403).
                body("code", equalTo(403)).
                body("reason", equalTo("Forbidden")).
                body("message", equalTo("Access Token is null.")).
            when().
                get("/modules/oauth2/http/resource");
    }

    @Test
    public void notAllowedWhenAccessTokenIsInvalid() {

        given().
                header("Authorization", "Bearer INVALID").
            expect().
                statusCode(403).
                body("code", equalTo(403)).
                body("reason", equalTo("Forbidden")).
                body("message", equalTo("Access Token is invalid.")).
            when().
                get("/modules/oauth2/http/resource");
    }
}
