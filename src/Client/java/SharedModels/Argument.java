/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedModels;
import java.io.Serializable;

/**
 *
 * @author Cagatay
 */
public class Argument implements Serializable{
    
    private static final long serialVersionUID = 5L;
    
    private int sentTime;
    private int stage;
    private String argument;
    
    public Argument(int sentTime,int stage,String argument){
        
        this.sentTime = sentTime ;
        this.stage = stage;
        this.argument = argument;
        
    }
    
}
