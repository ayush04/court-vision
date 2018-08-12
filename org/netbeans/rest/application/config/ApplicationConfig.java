/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author ayush
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.cv.attachment.AttachmentResource.class);
        resources.add(ws.cv.events.EventsResource.class);
        resources.add(ws.cv.game.GameResource.class);
        resources.add(ws.cv.notification.NotificationResource.class);
        resources.add(ws.cv.team.TeamResource.class);
        resources.add(ws.cv.usermgmt.PlayerProfileResource.class);
        resources.add(ws.cv.usermgmt.PlayerResource.class);
        resources.add(ws.cv.usermgmt.UserResource.class);
    }
    
}
