package org.dzcoinlab.client;

import org.dzcoinlab.core.exception.dzcoinexception;

import java.util.map;

public interface clientprocessor {

    static final string uri = "http://"+ org.dzcoinlab.client.util.config.getinstance().getproperty("client.server.host")+":"+ org.dzcoinlab.client.util.config.getinstance().getproperty("client.server.port");
    static final string admin_uri = "http://"+ org.dzcoinlab.client.util.config.getinstance().getproperty("client.server.admin.host") + ":"+ org.dzcoinlab.client.util.config.getinstance().getproperty("client.server.port");
    static final string model_server = org.dzcoinlab.client.util.config.getinstance().getproperty("model.api.server");

    public string processresponse(map<string, string> params) throws dzcoinexception;
}


