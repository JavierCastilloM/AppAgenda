/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package appagenda;

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author JavierCastilloM
 */
public class AppAgenda {

    public static void main(String[] args) {

//        Map<String, String> emfProperties = new HashMap<String, String>();
//        emfProperties.put("javax.persistence.schema-generation.database.action", "create");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();

//        Provincia provinciaSevilla = new Provincia();
//        provinciaSevilla.setNombre("Sevilla");
//        
        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
        queryProvinciaCadiz.setParameter("nombre", "Cádiz");
        List<Provincia> listProvinciasCadiz = queryProvinciaCadiz.getResultList();
        for (Provincia provinciaCadiz : listProvinciasCadiz) {
            System.out.println(provinciaCadiz.getId() + ":");
            System.out.println(provinciaCadiz.getNombre());
        }

        // Para modificar objetos y actualizar en la BD
//        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
//        queryProvinciaCadiz.setParameter("nombre", "Cádiz");
//        List<Provincia> listProvinciasCadiz = queryProvinciaCadiz.getResultList();
//        em.getTransaction().begin();
//        for (Provincia provinciaCadiz : listProvinciasCadiz) {
//            provinciaCadiz.setCodigo("CA");
//            em.merge(provinciaCadiz);
//        }
//        em.getTransaction().commit();
        // Para eliminar de la BD
//        Provincia provinciaId15 = em.find(Provincia.class, 15);
//        em.getTransaction().begin();
//        if (provinciaId15 != null) {
//            em.remove(provinciaId15);
//        } else {
//            System.out.println("No hay ninguna provincia con ID=15");
//        }
//        em.getTransaction().commit();
        em.getTransaction().begin();
//        em.persist(provinciaSevilla);
        em.getTransaction().commit();

        em.close();
        emf.close();
        try {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } catch (SQLException ex) {
        }
    }

}
