package uz.mirzokhidkh.springbootthreads.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import reactor.netty.transport.ProxyProvider;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${http.util.isSslVerificationDisabled}")
    private Boolean isSslVerificationDisabled;

    @Value("${use.proxy}")
    private Boolean useProxy;

    @Value("${proxy.ip}")
    private String proxyIp;

    @Value("${proxy.port}")
    Integer proxyPort;

    @Bean
    public WebClient webClient() {
        SslContext sslContext;
        try {
            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                    .trustManager(new TrustAllCertificates())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        /**end this is not used as it is deprecated*/
        /**this is updated way*/

        TcpClient tcpClient = TcpClient.create();
//                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        if (isSslVerificationDisabled) {
            tcpClient = tcpClient
                    /*this is for ignore ssl*/
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        }

//        SslProvider sslProvider = SslProvider.builder().sslContext(
//                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
//                )
//                .defaultConfiguration(SslProvider.DefaultConfigurationType.NONE).build();

        HttpClient httpClient = HttpClient.from(tcpClient)
//        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 90000)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(90000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(90000)))
//                /*this is for ignore ssl*/
//                .secure(sslProvider)
                ;
        //this is for configuring proxy
        if (useProxy) {
//            httpClient.proxy(typeSpec -> typeSpec.type(ProxyProvider.Proxy.HTTP).address(new InetSocketAddress(proxyIp, proxyPort)));
            httpClient = HttpClient.create()
                    .tcpConfiguration(tcpClient1 ->
                            tcpClient1
                                    .proxy(proxy -> proxy
                                            .type(ProxyProvider.Proxy.HTTP)
                                            .host(proxyIp)
                                            .port(proxyPort)));
        }
//
//        if (isSslVerificationDisabled) {
//            httpClient
//                    /*this is for ignore ssl*/
//                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
////                    .secure(sslContextSpec -> {
////                        try {
////                            sslContextSpec.sslContext(SslContextBuilder.forClient().trustManager(new TrustAllCertificates()).build());
////                        } catch (SSLException e) {
////                            throw new RuntimeException(e);
////                        }})
//        }


        /**this is updated way*/
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);


        WebClient webClient = WebClient.builder()
                /**this is for configuring memory size(response body size)
                 * https://stackoverflow.com/questions/59735951/databufferlimitexception-exceeded-limit-on-max-bytes-to-buffer-webflux-error*/
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                /**end this is for configuring memory size(response body size)*/
//                .baseUrl(baseUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .filter(logRequest())
//                .filter(logResponseStatus())
                .clientConnector(httpConnector)
                .build();

        return webClient;
    }


}
