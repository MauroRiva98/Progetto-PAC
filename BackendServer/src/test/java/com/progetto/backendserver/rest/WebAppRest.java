package com.progetto.backendserver.rest;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.ClientProtocolException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WebAppRest {
    @Autowired private DataSource dataSource;
    @Test
    public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
            throws ClientProtocolException, IOException {
        // Given
        //String name = RandomStringUtils.randomAlphabetic( 8 );
        //HttpUriRequest request = new HttpGet( "https://api.github.com/users/" + name );

        // When
        //HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );


        // Then
        //assertThat(
        //        httpResponse.getStatusLine().getStatusCode(),
        //        equalTo(HttpStatus.SC_NOT_FOUND));
    }
}
