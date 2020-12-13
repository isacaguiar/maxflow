/*
 * Created on 08/09/2005
 */
package org.jbpm.gpd.model.util;

/**
 * classe que controla a ordem das atividades e processos
 * @author Victor Franco Costa
 *
 */
public class Order {

    private static int next = 1;
    
    public static int next() {
        return next++;
    }
    
    public static void zera() {
    	next = 1;
    }
    
}
