package com.group3.server;

import com.group3.server.ir.ServerObserve;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.eclipse.leshan.server.model.VersionedModelProvider;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import java.util.Collection;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        String[] modelPaths = new String[]{"3001.xml", "3002.xml"};
        // Define model provider
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models/", modelPaths));
        LwM2mModelProvider modelProvider = new VersionedModelProvider(models);
        LeshanServerBuilder builder = new LeshanServerBuilder();
        builder.setObjectModelProvider(modelProvider);
        final LeshanServer server = builder.build();
        server.start();

        final ServerObserve serverObserve = new ServerObserve();

        server.getRegistrationService().addListener(new RegistrationListener() {
            public void registered(Registration registration, Registration registration1, Collection<Observation> collection) {
                System.out.println("New Device: " + registration.getEndpoint());
                System.out.println("New Device ID: " + registration.getId());

                if(registration.getEndpoint().equals("Sound Sensor")){
                    try {
                        // Observe An Object
                        serverObserve.observe(server, server.getRegistrationService().getByEndpoint("Sound Sensor"), 3001, 0, 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void updated(RegistrationUpdate registrationUpdate, Registration registration, Registration registration1) {

            }

            public void unregistered(Registration registration, Collection<Observation> collection, boolean b, Registration registration1) {

            }
        });
    }
}
