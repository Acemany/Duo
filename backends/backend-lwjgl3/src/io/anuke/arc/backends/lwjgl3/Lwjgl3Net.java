/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package arc.backends.lwjgl3;

import arc.Net;
import arc.net.*;
import arc.util.SharedLibraryLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * LWJGL implementation of the {@link Net} API, it could be reused in other Desktop backends since it doesn't depend on LWJGL.
 * @author acoppes
 */
public class Lwjgl3Net implements Net{
    NetJavaImpl netJavaImpl = new NetJavaImpl();

    @Override
    public void sendHttpRequest(HttpRequest httpRequest, HttpResponseListener httpResponseListener){
        netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
    }

    @Override
    public void cancelHttpRequest(HttpRequest httpRequest){
        netJavaImpl.cancelHttpRequest(httpRequest);
    }

    @Override
    public ServerSocket newServerSocket(Protocol protocol, String ipAddress, int port, ServerSocketHints hints){
        return new NetJavaServerSocketImpl(protocol, ipAddress, port, hints);
    }

    @Override
    public ServerSocket newServerSocket(Protocol protocol, int port, ServerSocketHints hints){
        return new NetJavaServerSocketImpl(protocol, port, hints);
    }

    @Override
    public Socket newClientSocket(Protocol protocol, String host, int port, SocketHints hints){
        return new NetJavaSocketImpl(protocol, host, port, hints);
    }

    @Override
    public boolean openURI(String url){
        try{
            if(SharedLibraryLoader.isMac){
                Class.forName("com.apple.eio.FileManager").getMethod("openURL", String.class).invoke(null, url);
                return true;
            }else if(SharedLibraryLoader.isLinux){
                exec("xdg-open " + url);
                return true;
            }else if(SharedLibraryLoader.isWindows){
                exec("rundll32 url.dll,FileProtocolHandler " + url);
                return true;
            }
            return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }

    private void exec(String command) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

}
