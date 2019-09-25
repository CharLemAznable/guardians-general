package com.github.charlemaznable.guardians.general.riven;

import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class AppDao {

    public static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe6qiLgfEZQGu0Z3ewLF50OXQmowHQ60C8a49nIxGmNQzGOZqSCbD/2Seo6Hc7miyWJ+cYJjOG7iU0GZLAeJDr7QulQfgNoXnQ6DpYFoGpG97FK/biFpgLujJTxIe1GgX0mAiUFjNKU+2N97a5SwGQElrIsd31hw/Bvp1qkvbn8QIDAQAB";
    public static final String PRV_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ7qqIuB8RlAa7Rnd7AsXnQ5dCajAdDrQLxrj2cjEaY1DMY5mpIJsP/ZJ6jodzuaLJYn5xgmM4buJTQZksB4kOvtC6VB+A2hedDoOlgWgakb3sUr9uIWmAu6MlPEh7UaBfSYCJQWM0pT7Y33trlLAZASWsix3fWHD8G+nWqS9ufxAgMBAAECgYEAkMhBnjhYrCTg+494n+0McN8XT0wbj3eFlyR3lgOBROf4J3C7vmlN9Qqm82A3oPmpvXPFO18yeq/k/T5/UROHE7tHYpNFlqmqTcnw5itvtjdBCH69Rz26yXWHvSR2i7py/yVz9wT4Z5L0ZRIXLK19RB18ZzPuUYBIbqKZBtwZzXECQQDPyMnuApagwJR6clanfBgMuXwoWDJKaU5ajwg0jB/eVdClZAPnRhEDaBfQd1O6s/Bt4vSIjscijF78aoUHo/DPAkEAw8rvYdmJQPa9H1M2dd+sxzihss0Eky8ado/9ZCpeICIdBGoHHY5Y1tde1euxNcbISypF3YQvpozSrztyxU5LPwJAR9Sh+iXq7KemrK3aRwwfREbbJRdYS2EcNTI9I+1NFw+TkZmBv/H477SMhwS5bVHHTDWpU4wIS2k7bQdl9b43AQJAOyNJqMF/Dos4VoTY10OnOUlmtc3tCtPYZ2VYgO+3HHeRUisAWTIMrV/B+lVFYlvXVbTAA4eQqPqyDFGleekLGwJAazC7XIABwJ36LUqB4ZbVl1NARxsutqDTCs1hhNV+hOWElJrAtc/NFK54vqF5M4asMKk9UA5I0pTwk2BDxNtb+Q==";

    App queryApp(String appId) {
        val app = new App();
        app.setId("test");
        app.setName("test");
        app.setPubKey(PUB_KEY);
        app.setPrvKey(PRV_KEY);
        app.setSignKey(PUB_KEY);
        return app;
    }
}
