package com.uboxol.cloud.mermaid.cfg;

import com.uboxol.cloud.mermaid.service.remote.CdsApiTransportServiceConfig;
import com.uboxol.cloud.mermaid.service.remote.NodeTransportServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:27
 */
@Configuration
@Import({
    CdsApiTransportServiceConfig.class,
    NodeTransportServiceConfig.class
})
public class RemoteThriftAutoConfiguration {
}
