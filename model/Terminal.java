package model;

//import java.util.ArrayList;

//import java.util.ArrayList;
//import java.util.Collections;

public class Terminal {
	
	//TERMINALES==========================================================

	//[T][ADD] 	Agrega un vertice LIBRE Aleatorio a la solucion, FALSE si no se puede agregar
	public static boolean addFreeVertRandom(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[ADD][addFreeVertRandom]:  ");System.out.println("Maximal");
			return false;}//si es maximal, no hay vertices libres que agregar
		int index_v = (int) Math.floor(Math.random()*(is.gettam_free())+is.gettam_sol());
		if(index_v <is.gettam_sol() || index_v >(is.gettam_sol()+is.gettam_free()-1)){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[ADD][addFreeVertRandom]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.insertVert(index_v);	//Agregarlo en la Solución
		return true;
	}
	//[T][ADD]	Agrega el vertice LIBRE con MENOS vecinos a la solución, FALSE si no se puede agregar
	public static boolean addFreeVertFewerNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[ADD][addFreeVertFewerNeighbors]:  ");System.out.println("FALSE:Maximal");
			return false;}//si es maximal, no hay vertices libres que agregar
		int index_v = is.getFreeVertFewerNeighbors();//indice del vertice libre con menos vecinos
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[ADD][addFreeVertFewerNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"] ");
		is.insertVert(index_v);	//Agregarlo en la Solución
		return true;
	}
	//[T][ADD]	Agrega el vertice LIBRE con MAS vecinos a la solución, FALSE si no se puede agregar
	public static boolean addFreeVertMoreNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[ADD][addFreeVertMoreNeighbors]:  ");System.out.println("FALSE:Maximal");
			return false;}//si es maximal, no hay vertices libres que agregar
		int index_v = is.getFreeVertMoreNeighbors();//indice del vertice libre con mas vecinos
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[ADD][addFreeVertMoreNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.insertVert(index_v);	//Agregarlo en la Solución
		return true;
	}
	//[T][ADD]	Agrega el vertice LIBRE con MENOS vecinos LIBRES a la solución, FALSE si no se puede agregar
	public static boolean addFreeVertFewerFreeNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[ADD][addFreeVertFewerFreeNeighbors]:  ");System.out.println("FALSE:Maximal");
			return false;}//si es maximal, no hay vertices libres que agregar
		int index_v = is.getFreeVertFewerFreeNeighbors();//indice del vertice libre con menos vecinos libres
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[ADD][addFreeVertFewerFreeNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.insertVert(index_v);	//Agregarlo en la Solución
		return true;
	}
	//[T][IS] Pregunta si La solucion es MAXIMAL, TRUE o FALSE
	public static boolean isMaximalSol(IndependentSet is){
		//System.out.println("[IS][isMaximalSol]:"+is.isMaximal());
		return is.isMaximal();
	}
	//[T][IS] Pregunta si la solucion es VACIA, TRUE o FALSE
	public static boolean isVoid_Sol(IndependentSet is){
		//System.out.println("[IS][isVoid_Sol]:"+is.isVoid());
		return is.isVoid();
	}
	//[T][RMV] 	Remueve un vertice solucion aleatorio, FALSE si no se puede remover
	public static boolean removeSolVertRandom(IndependentSet is){
		if(is.isVoid()) {
			//System.out.print("[RMV][removeTheFirstSolVert]:  ");System.out.println("Void");
			return false;}//si es vacio, no hay vertices sol que remover
		int index_v = (int) Math.floor(Math.random()*(is.gettam_sol()));
		if(index_v < 0 || index_v > (is.gettam_sol()-1)){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[RMV][removeTheFirstSolVert]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.removeVert(index_v);	//Remover de la Solución
		return true;
	}
	//[T][RMV]	Remueve el vertice sol con mas vecinos, FALSE si no se puede remover
	public static boolean removeSolVertMoreNeighbors(IndependentSet is){
		if(is.isVoid()) {
			//System.out.print("[RMV][removeSolVertMoreNeighbors]:  ");System.out.println("Void");
			return false;}//si la solución es vacia, no hay vertices sol que remover
		int index_v = is.getSolVertMoreNeighbors();//indice del vertice solucion con mas vecinos
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[RMV][removeSolVertMoreNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.removeVert(index_v);//Removerlo de la Solución
		return true;
	}
	//[T][RMV]	Remueve el vertice sol con menos vecinos, FALSE si no se puede remover
	public static boolean removeSolVertFewerNeighbors(IndependentSet is){
		
		if(is.isVoid()) {
			//System.out.print("[RMV][removeSolVertFewerNeighbors]:  ");System.out.println("Void");
			return false;}//si la solución es vacia, no hay vertices sol que remover
		int index_v = is.getSolVertFewerNeighbors();//indice del vertice solucion con menos vecinos
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
			return false;}
		//System.out.print("[RMV][removeSolVertFewerNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.removeVert(index_v);//Removerlo de la Solución
		return true;
	}
	//[T][RMV]	Remueve el vertice sol con mas vecinos 1_Tight, FALSE si no se puede remover
	public static boolean removeSolVertMore1_TightNeighbors(IndependentSet is){
		if(is.isVoid()) {
			//System.out.print("[RMV][removeSolVertMore1_TightNeighbors]:  ");System.out.println("Void");
			return false;}//si la solución es vacia, no hay vertices sol que remover
		int index_v = is.getSolVertMore1_TightNeighbors();//indice del vertice solucion con mas vecinos
		if(index_v ==-1){
			System.out.println("ERROR! indice incorrecto!");
		return false;}
		//System.out.print("[RMV][removeSolVertMore1_TightNeighbors]:  ");System.out.println("["+is.getSol().get(index_v)+"]");
		is.removeVert(index_v);//Removerlo de la Solución
		return true;
	}	
	///[T][IMPRV] Busca una Mejora_2, TRUE si la realiza, FALSE si no la encuentra.  
	public static boolean Mejora_2_original(IndependentSet is){
		//System.out.print("[IMPRV][Mejora_2]:  ");
		return is.Mejora_2_original();
	}
	//[T][IMPRV] Busca una Mejora_2 version Directa, TRUE si la realiza, FALSE si no la encuentra.  
	public static boolean Improvements2_Maximal(IndependentSet is){
		if(!is.isMaximal()) {
			//System.out.print("[IMPRV][Improvements2_Maximal]:  ");System.out.println("FALSE:Maximal");
		return false;}
		//System.out.print("[IMPRV][Improvements2_Maximal]:  ");
		return is.Mejora_2_Maximal();
	}  
	//[T][IMPRV] Busca una Mejora_3, TRUE si la realiza, FALSE si no la encuentra.  
	public static boolean Improvements3_Maximal(IndependentSet is){
		if(!is.isMaximal()) {
			//System.out.print("[IMPRV][Improvements3_Maximal]:  ");System.out.println("FALSE:Maximal");
			return false;}
		//System.out.print("[IMPRV][Improvements3_Maximal]:  ");
		return is.Mejora_3_Maximal();
	}
	//[T][IMPRV] Busca una Mejora 2 o 3, TRUE si la realiza, FALSE si no la encuentra.  
	public static boolean OneImprovements2or3_Maximal(IndependentSet is){
		if(!is.isMaximal()) {
			//System.out.print("[IMPRV][OneImprovements2or3_Maximal]:  ");System.out.println("FALSE:Maximal");
			return false;}
		//System.out.print("[IMPRV][OneImprovements2or3_Maximal]:  ");
		return is.M_2o3_Maximal();
	}
	//[T][IMPRV] Busca todas las Mejoras 2 posibles, TRUE si la realiza al menos una, FALSE si no encuentra.  
	public static boolean All_Imprv2_Maximal(IndependentSet is){
		if(!is.isMaximal()) {return false;}
		return is.Ms_2_Maximal();
	}
	//[T][IMPRV] Busca todas las Mejoras 3 posibles, TRUE si la realiza al menos una, FALSE si no encuentra.  
	public static boolean All_Imprv3_Maximal(IndependentSet is){
		if(!is.isMaximal()) {return false;}
		return is.Ms_3_Maximal();
	}
	//[T][IMPRV] Busca todas las Mejoras 2 y 3 posibles, TRUE si realiza al menos una, FALSE si no encuentra.  
	public static boolean All_Imprv2and3_Maximal(IndependentSet is){
		if(!is.isMaximal()) {return false;}//si no es maximal no entra
		return is.Ms_2y3_Maximal();
	}
	//[T][BUILDER] Construye una solución, agregando vertices aleatorios.  
	public static boolean Builder_for_Random(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[BLDR][Builder_for_Random]:  ");System.out.println("FALSE:Maximal");
			return false;}
		//System.out.print("[BLDR][Builder_for_Random]:  ");System.out.println("TRUE");
		is.llenar_por_vertice_libre_aleatorio();
		return true;
	}
	//[T][BUILDER] Construye una solución, agregando vertices segun el criterio del vertice con más vecinos.  
	public static boolean Builder_for_MoreNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[BLDR][Builder_for_MoreNeighbors]:  ");System.out.println("FALSE:Maximal");
		return false;}
		//System.out.print("[BLDR][Builder_for_MoreNeighbors]:  ");System.out.println("TRUE");	
		is.llenar_por_mayor_num_vecinos();
		return true;
	}
	//[T][BUILDER] Construye una solución, agregando vertices segun el criterio del vertice con menos vecinos.  
	public static boolean Builder_for_FewerNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[BLDR][Builder_for_FewerNeighbors]:  ");System.out.println("FALSE:Maximal");
			return false;}
		//System.out.print("[BLDR][Builder_for_FewerNeighbors]:  ");System.out.println("TRUE");
		is.llenar_por_menor_num_vecinos();
		return true;
	}
	//[T][BUILDER] Construye una solución, agregando vertices segun el criterio del vertice con menos vecinos.  
	public static boolean Builder_for_FewerFreeNeighbors(IndependentSet is){
		if(is.isMaximal()) {
			//System.out.print("[BLDR][Builder_for_FewerFreeNeighbors]:  ");System.out.println("FALSE:Maximal");
		return false;}
		//System.out.print("[BLDR][Builder_for_FewerFreeNeighbors]:  ");System.out.println("TRUE");
		is.llenar_por_menor_num_vecinos_libres();
		return true;
	}
	//[T][METAHEURISTIC] Realiza perturbaciones seguido de mejoras, mantiene la mejor solucion obtenida.
	public static boolean ILS(IndependentSet is){
		if(!is.isMaximal()) {
			//System.out.print("[METAHEURISTIC][ILS]:  ");System.out.println("FALSE:Maximal");
			return false;}
		//System.out.print("[METAHEURISTIC][ILS]:  ");
		is.ILS();
		return true;
	}
}
