package com.crudite.apps.entite;

import com.crudite.apps.R;
import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;
import com.crudite.apps.models.modelAnotationType;

import java.util.ArrayList;

/**
 * Created by root on 26/07/18.
 */
//@modelAnotationType(remoteSqlQuery = "")
public class Contenu extends BaseModel{
    @modelAnotationField(columnName = "idcontenu_APP", primaryKey = true)
    public int id_categorie_app;
    @modelAnotationField(columnName = "idcontenu")
    public int id_categorie;
    @modelAnotationField(columnName = "intitule")
    public String nom_categorie;
    @modelAnotationField(columnName = "image")
    public String image;
    @modelAnotationField(columnName = "description")
    public String description;
    public String subtext;
    public int imageRessource;

    public static ArrayList<Contenu> testCategorie(){
        ArrayList<Contenu> list = new ArrayList<>();
        Contenu cat = new Contenu();
        cat.id_categorie = 1;
        cat.nom_categorie = "Assiette Crudités Multi Saveurs";
        cat.image = "assiettes.jpg";
        cat.imageRessource = R.drawable.assiette;
        cat.subtext = "Riche en antioxydants, vitamines, " +
                "minéraux, protéines et autres nutriments, " +
                "assurant ainsi une alimentation équilibrée et une bonne santé";
        list.add(cat);
        cat = new Contenu();
        cat.id_categorie = 2;
        cat.nom_categorie = "Vinaigrette aux fines herbes";
        cat.image = "vinaigrette.jpg";
        cat.imageRessource = R.drawable.vinaigrette;
        cat.subtext = "En plus de sa richesse en acides gras Omega, vitamines, minéraux et antioxydants qui contribuent " +
                "au bon fonctionnement du cœur et du cerveau, son zeste authentique donnera du sens à vos assaisonnements.";
        list.add(cat);
        cat = new Contenu();
        cat.id_categorie = 3;
        cat.nom_categorie = "Salade de Fruits";
        cat.image = "salade.jpg";
        cat.imageRessource = R.drawable.salade;
        cat.subtext = "Très riche en antioxydants essentiels  dans la prévention des maladies chroniques, " +
                "de par ce  plat vous pourriez apprécier en plus de ses vertus thérapeutiques le gout unique et" +
                " exotique des fruits de saison.";
        list.add(cat);
        cat = new Contenu();
        cat.id_categorie = 4;
        cat.nom_categorie = "Grignote";
        cat.image = "grinote.jpg";
        cat.imageRessource = R.drawable.grinote;
        cat.subtext = "Cacahuètes caramélisées, Noix de coco caramélisée...";
        list.add(cat);
        return list;
    }
}
