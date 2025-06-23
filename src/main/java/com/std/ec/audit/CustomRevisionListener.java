package com.std.ec.audit;

import org.hibernate.envers.RevisionListener;
//import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity rev = (CustomRevisionEntity) revisionEntity;
        String username = System.getProperty("usuario.actual", "usuario_por_defecto");

        // Obtener el usuario autenticado desde Spring Security
       /* String username = "anonimo";
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                username = auth.getName();
            }
        } catch (Exception e) {
            // Ignorar si no hay contexto de seguridad
        }*/

        rev.setUsername(username);
    }
}
