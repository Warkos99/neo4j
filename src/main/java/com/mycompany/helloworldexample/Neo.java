/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helloworldexample;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;
/**
 *
 * @author arcem
 */
public class Neo implements AutoCloseable{
   private Driver driver;  
   public Neo ( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
        
        
    }
    @Override
    public void close() throws Exception {
        driver.close ();
        
    }
public void crearRelacion(String idreceta,String idingrediente, int gramos){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("MATCH (r:receta) WHERE id(r)="+idreceta+" "
              +"MATCH (i:ingrediente) WHERE id(i)="+idingrediente+" "+
              "CREATE(r)-[relacion:contiene{cantidad:"+gramos+"}]->(i)");
            return"";
            
        }
    });
 s.close();
}    
public void crearRelacion2(String idingrediente,String idintolerancia){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("MATCH (r:ingrediente) WHERE id(r)="+idingrediente+" "
              +"MATCH (i:intolerancia) WHERE id(i)="+idintolerancia+" "+
              "CREATE(r)-[:ocasiona]->(i)");
            return"";
            
        }
    });
 s.close();
}
public void crearRelacion3(String idrecet,String idtip){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = tx.run("MATCH (r:receta) WHERE id(r)="+idrecet+" "
              +"MATCH (i:tipo) WHERE id(i)="+idtip+" "+
              "CREATE(r)-[:Pertenece]->(i)");
            return"";
            
        }
    });
 s.close();
}
public void insertarIngrediente(String nombre){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(i:ingrediente {nombre: $nombre})",parameters("nombre", nombre));
            return"";
            
        }
    });
 s.close();
}


public List<Record> getIngredientes (){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(i:ingrediente) RETURN id(i),i.nombre");
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}


public void borrarrelacion1( final String id,final String idr){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (j:receta)-[r:contiene]->(i:ingrediente) WHERE id(i)="+id+" and id(j)="+idr+"  DETACH DELETE r";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }  
public void borrarrelacion2( final String id,final String idr){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (j:ingrediente)-[r:ocasiona]->(i:intolerancia) WHERE id(j)="+id+" and id(i)="+idr+"  DETACH DELETE r";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
}
    public void borrarrelacion3( final String id,final String idr){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (j:receta)-[r:Pertenece]->(i:tipo) WHERE id(j)="+id+" and id(i)="+idr+"  DETACH DELETE r";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }  
public void borrarnodoIngrediente( final String id){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (i:ingrediente) WHERE id(i)="+id+"DELETE i";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }    

public void insertarIntolerancia(String nombre){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(in:intolerancia {nombre: $nombre})",parameters("nombre", nombre));
            return"";
            
        }
    });
 s.close();
}

public List<Record> getIntolerancia (){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(in:intolerancia) RETURN id(in),in.nombre");
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}

public void borrarnodoIntolerancia( final String id){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (in:intolerancia) WHERE id(in)="+id+"DELETE in";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }

public void eliminarTodo(){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("MATCH (r) DETACH DELETE r");
            return"";
            
        }
    });
 s.close();
}

public void insertarTipo(String nombre){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(t:tipo {nombre: $nombre})",parameters("nombre", nombre));
            return"";
            
        }
    });
 s.close();
}

public List<Record> getTipo (){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(t:tipo) RETURN id(t),t.nombre");
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}

public void borrarnodoTipo( final String id){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (t:tipo) WHERE id(t)="+id+"DELETE t";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }

public void borrarnodoReceta( final String id){
 
    try (Session s= driver.session() )
    {
        String greeting = s.writeTransaction( new TransactionWork<String>()
        {
            @Override 
            public String execute (Transaction tx)
            {
                String cypher ="MATCH (r:receta) WHERE id(r)="+id+"DELETE r";
                System.out.println(cypher);
                   Result result = tx.run(cypher);
                   tx.commit();
                   return result.single().get( 0 ).asString();
                   
            }
        });
        s.close();
    };
    
    }

public List<Record> getReceta (){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(r:receta) RETURN id(r),r.nombre");
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}
public List<Record> getRecetaEdit (String ing){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(a:receta)-[r:contiene]->(b:ingrediente{nombre:$ing}) RETURN id(a),a.nombre",parameters("ing",ing));
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}
public List<Record> getRecetaEdit2 (int ing){
    Session s=driver.session();
    List<Record> lista=new ArrayList();
  Result resultado = s.run("MATCH(a:receta)WHERE a.tiempo<"+ing+" RETURN id(a),a.nombre",parameters("ing",ing));
      
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
          s.close();
            return lista;                            
}

public void insertarReceta(String nombre,String dificultad,int minutos){
    Session s=driver.session();
    String trans = s.writeTransaction(new TransactionWork<String>(){
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(r:receta {tiempo: $tiempo,dificultad:$dificultad,nombre:$nombre})",parameters("tiempo",minutos,"dificultad",dificultad,"nombre",nombre));
            return"";
            
        }
    });
 s.close();
}

public static String limpiar(String texto){
           String quitarComillas="";
           for (int i = 0; i < texto.length(); i++) {
               if(texto.charAt(i)!='"'){
                    quitarComillas+=texto.charAt(i);
               }
              
           }
           return quitarComillas;
       }
}





