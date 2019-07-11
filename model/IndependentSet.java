package model;

import java.util.ArrayList;
import java.util.List;

public class IndependentSet {

	private int nVert;				//Numero de vertices del problema
	private int mArist;				//Numero de aristas del problema
	private ArrayList<ArrayList<Integer>> AdjacencyList; //Lista de ayacencia entre todos los vertices del grafo
	private int tam_sol; 			//tamaño del primer bloque, vertices solucion
	private int tam_free;			//tamaño del segundo bloque, vertices libres
	private ArrayList<Integer> VertexList; //Lista de vertices solucion, libres y descartados
	private ArrayList<Integer> TightList;	//	Tightness de cada vertice
	private ArrayList<Integer> NumberNeighborsList;	//Lista de vertices ordenados segun cantidad de vecinos
	private ArrayList<Integer> StatusList;	//Lista de ceros, unos y dos q indican que vertices son libres, solucion o niguno de los dos
	private IndependentSet original_sol;
	private ArrayList<Integer> conjuntosolucion; 
	private double currentProfit;			//Numero de vertices en la solución, cardinalidad del conjunto independiente

	public IndependentSet() {
		this.tam_sol = 0;
		this.currentProfit = 0;
		AdjacencyList 			= new ArrayList<ArrayList<Integer>>();
		VertexList 				= new ArrayList<Integer>();
		TightList 			= new ArrayList<Integer>();
		StatusList			= new ArrayList<Integer>();
		NumberNeighborsList	= new ArrayList<Integer>();
		conjuntosolucion = new ArrayList<Integer>();
	}
	public void UpdateIS_1() {
		//Si ya se posee el numero de vertices, se actuliza
		this.tam_free = nVert;
		for(int i=0; i<nVert;i++){	//Inicializa y setea los listas de: Adyacencia, Solucion, Tighness, pertenencia de bloque
			AdjacencyList.add(new ArrayList<Integer>());

		}
	}
	public void UpdateIS_2(){
		//System.out.println("	Setiando: Sol,TightList,StatusList,NumNeighborsList...");
		for(int i=0; i<nVert;i++){
			VertexList.add(i);	//Los vertices parten del vertice cero 
			TightList.add(0);	//Todos los vertices parten con ajuste cero, todos parten libres
			StatusList.add(0);	//Todos los vertice parten con status cero, todos parten libres
			NumberNeighborsList.add(i);	//Se inicializa con todos los vertices, para ser luego ordenada por numero de vecinos
		}
		//System.out.print("	Ordenando: NumNeighborsList... ");
		IterativeQuicksort_NNList();
	}
	//GETTERS============================================================
	public int getnVert() 			{return nVert;}
	public int getmArist() 			{return mArist;}
	public int gettam_sol() 		{return tam_sol;}
	public int gettam_free()		{return tam_free;}
	public double getCurrentProfit()	{return currentProfit;}
	public ArrayList<Integer> getSol() 			{return VertexList;}
	public ArrayList<Integer> getTight_vert()	{return TightList;}
	public ArrayList<Integer> getConjuntoSolucion()	{return conjuntosolucion;}
	//SETTERS============================================================
	public void setnVert(int nVert)				{this.nVert = nVert;}
	public void setmArist(int mArist)			{this.mArist = mArist;}
	public void settam_sol(int limit_sol)		{this.tam_sol = limit_sol;}
	public void settam_free(int limit_free)	{this.tam_free = limit_free;}
	public void setCurrentProfit(double currentProfit) 			{this.currentProfit = currentProfit;}
	public void setBestSol(IndependentSet is){
		this.tam_sol = is.tam_sol;
		this.tam_free = is.tam_free;
		this.currentProfit = is.currentProfit;
		this.VertexList = is.VertexList;
		this.TightList = is.TightList;
		this.StatusList = is.StatusList;
		this.conjuntosolucion = is.conjuntosolucion;
	}
	//OTHERS============================================================
	public void addAdyacencia(int v1,int v2 ){//Agrega adyacencia entre los vertices
		if(this.AdjacencyList.get(v1-1).contains(v2-1))//si la adyacencia ya existe
			return;
		this.AdjacencyList.get(v1-1).add(v2-1);
		this.AdjacencyList.get(v2-1).add(v1-1);
	}
	public void Bulblesort(){
		boolean changed;
		do {
			changed = false;

			for (int i = 0; i < nVert - 1; i++) {
				int a = NumberNeighborsList.get(i);
				int b = NumberNeighborsList.get(i+1);
				int a_vec = AdjacencyList.get(a).size();
				int b_vec = AdjacencyList.get(b).size();
				if( a_vec > b_vec){
					NumberNeighborsList.set(i, b);
					NumberNeighborsList.set(i+1, a);
					changed = true;
				}
			}
		} while (changed);
	}
	public void IterativeQuicksort_NNList(){
		int largo = this.nVert;
		ArrayList<Boolean> sorted = new ArrayList<Boolean>(largo);
		for(int k=0;k<largo;k++){
			sorted.add(false);
		}
		int i, j, sortedCount = 0;
		while (sortedCount < largo) {
			for (i=0; i<largo; i++)
				if (!sorted.get(i)) {
					for (j=i; (j<(largo-1)) && (!sorted.get(j+1)); j++) ;
					sorted.set(i = partition(i,j), true);
					sortedCount++;
				}
		}
	}
	private int partition(int left, int right) {
		// DK:  added check if (left == right):
		if (left == right) return left;
		int i = left - 1;
		int j = right;
		while (true) {
			while (AdjacencyList.get(NumberNeighborsList.get(++i)).size() < AdjacencyList.get(NumberNeighborsList.get(right)).size())       // find item on left to swap
				;                           // a[right] acts as sentinel
			while (AdjacencyList.get(NumberNeighborsList.get(right)).size() < AdjacencyList.get(NumberNeighborsList.get(--j)).size())       // find item on right to swap
				if (j == left) break;       // don't go out-of-bounds
			if (i >= j) break;              // check if pointers cross
			swap_qs(i, j);                  // swap two elements into place
		}
		swap_qs(i, right);                  // swap with partition element
		return i;
	}
	public void swap_qs(int i, int j) {
		int temp = NumberNeighborsList.get(i);
		NumberNeighborsList.set(i, NumberNeighborsList.get(j));
		NumberNeighborsList.set(j,temp);
	}
	public int getIdVertNeigh(int index_id, int index_n){//Retorna el id del vertice vecino n, del vertice index_id
		return AdjacencyList.get(index_id).get(index_n);
	}
	public int getIndexVert(int id){//Retorna el indice del vertice id
		return VertexList.indexOf(id);//se utiliza el vertice cero
	}
	public int getIndexVert(int id_v, int id_b){//Retorna el indice del vertice id seleccionando el bloque de busqueda (mas eficiente)
		switch(id_b) {
		case 1:
			for(int i=0; i<this.tam_sol; i++){
				if(VertexList.get(i) == id_v)
					return i;}
			return -1;
		case 2: 
			for(int i=this.tam_sol; i<(this.tam_free+this.tam_sol);i++){
				if(VertexList.get(i) == id_v)
					return i;}
			return -1;
		case 3: 
			for(int i=(this.tam_free+this.tam_sol); i<this.nVert;i++){
				if(VertexList.get(i) == id_v)
					return i;}
			return -1;
		default: 
			return -1;
		}
	}
	public void updateTightness(){
		for(int i=0; i<nVert;i++){//inicializa todos lo valores a cero
			TightList.set(i, 0);//alteranitva B
		}
		for(int i=0; i<tam_sol;i++){//recorre los vertices sol
			int id_vert_sol = VertexList.get(i);//vertice de la posicion i
			for(int j=0; j<AdjacencyList.get(id_vert_sol).size(); j++){//recorre las adyacencias de un vertice solución
				int id_vert_ady = AdjacencyList.get(id_vert_sol).get(j);//id del vertices adyacente j al vertices sol
				int tight = TightList.get(id_vert_ady);//tightness del vertice adyacente
				TightList.set(id_vert_ady, tight+1);//se incrementa el tightness del vertices adyacente +1
			}
		}
	}
	public void swap_is(int index1, int index2){//Intercambia los id de los vertices segun sus indices
		if(index1==index2)return;//si los indices son iguales no intercambiar nada
		int aux = VertexList.get(index1);//se guarda el id del vertice con indice1
		VertexList.set(index1, VertexList.get(index2));//se modifica el vertice con indice1 con el id del vertice con indice2
		VertexList.set(index2, aux);//Se modifica el vertice con indice2 con el id del vertice guardado
	}
	public void insertVert(int index_v){//el vertice previamente debe ser libre, 2° bloque
		int id_v = VertexList.get(index_v);//Se obtiene el id del vertice V a insertar antes que se pierda por hacer swap

		//1° Se mueve V al 1° bloque
		swap_is(this.tam_sol,index_v);//se cambia v por el primer vertice del 2°bloque
		this.StatusList.set(id_v, 1);//vertice id_v pertenece a la solución
		//se cambia el limite entre los bloques para hacer a v el ultimo vertice del 1° bloque
		conjuntosolucion.add(id_v);
		this.tam_sol++;//la solucion se incrementa por uno
		this.tam_free--;//existen un vertice libre menos
		//2° Para cada vecino w de v se aumenta su tightness por uno
		ArrayList<Integer> noFree = new ArrayList<Integer>();
		for(int i=0;i<this.AdjacencyList.get(id_v).size();i++){
			int id_vert_ady = AdjacencyList.get(id_v).get(i);//id del vertices adyacente j al vertice sol
			int tight = TightList.get(id_vert_ady);//tightness del vertice adyacente
			TightList.set(id_vert_ady, tight+1);//se incrementa el tightness del vertices adyacente +1
			if(tight==0) noFree.add(id_vert_ady);//si su tightness deja de ser cero se debe mover al 3°bloque
		}
		//3° Se mueven los w al 3° bloque que eran previamente libres
		for(int i=0; i<noFree.size();i++){
			int id_w = noFree.get(i);
			//int index_w =this.getIndexVert(id_w);
			int index_w =this.getIndexVert(id_w,2);
			swap_is(index_w , this.tam_sol+this.tam_free-1);//se cambia w por el ultimo vertice del 2°bloque
			this.StatusList.set(id_w, 2);//vertice id_w ya no es libre
			//se cambia el limite entre los bloques para hacer a v el primer vertice del tercer bloque
			this.tam_free--;//existen un vertice libre menos
		}

		this.currentProfit+=1;
	}
	public void removeVert(int index_v){//el vertice previamente debe ser parte de la Sol, 1° bloque
		int id_v = VertexList.get(index_v);//se obtiene el id_v antes que se pierda por hacer swap	
		//1° Se mueve v al 2° bloque de la sol
		swap_is(index_v,this.tam_sol-1);//se cambia v por el ultimo vertice del 1°bloque
		this.StatusList.set(id_v, 0);//vertice id_v ya no pertenece a la solución
		//se cambia el limite entre los bloques para hacer a v el primer vertice del 2° bloque
		int idd = conjuntosolucion.indexOf(id_v);
		conjuntosolucion.remove(idd);
		this.tam_sol--;//la solucion disminuye en uno
		this.tam_free++;//existen un vertice libre mas
		//2° Para cada vecino w de v se disminuye su tightness por uno
		ArrayList<Integer> Free = new ArrayList<Integer>();
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		int numNeighbors = this.AdjacencyList.get(id_v).size();
		for(int i=0;i<numNeighbors;i++){
			int id_neighbor = AdjacencyList.get(id_v).get(i);//id del vertices adyacente j al ex_vertice sol
			int tight = TightList.get(id_neighbor);//tightness del vertice adyacente
			if(tight==1) Free.add(id_neighbor);// si su tight es 1, ahora es 0 y se debe mover al 2° bloque
			TightList.set(id_neighbor, tight-1);//se disminuye el tightness del vertices adyacente -1

			if(tight==2) candidates.add(id_neighbor);//PARA LA LISTA DE CANDIDATOS
		}		
		//3° Se mueven los w al 2° bloque que su nuevo tightness es cero
		for(int i=0; i<Free.size();i++){
			int id_w = Free.get(i);
			//int index_w =this.getIndexVert(id_w); 
			int index_w =this.getIndexVert(id_w,3);
			swap_is(this.tam_sol+this.tam_free, index_w);//se cambia w por el primer vertice del 3°bloque
			this.StatusList.set(id_w, 0);//vertice id_w es libre
			//se cambia el limite entre los bloques para hacer a v el ultimo vertice del 2° bloque
			this.tam_free++;//existen un vertice libre mas
		}

		this.currentProfit-=1;
	}
	public boolean isMaximal(){//Es Maxinal, si no quedan vertices libres por agregar
		//Si no quedan vertices libres tam_free es 0
		if(this.tam_free == 0)
			return true;
		return false;				
	}
	public boolean isVoid(){//Es vacio, si el conjunto solución es vacio, todos los vértices son libres
		if(this.tam_sol == 0)//tamaño de la solucion es cero, o todos los vertices son libres
			return true;
		return false;				
	}
	public boolean isFreeVert(int id_v){
		if(this.StatusList.get(id_v)==0)	return true;
		return false;
	}
	public boolean isSolVert(int id_v){//Es id_v un vertice solución
		if(this.StatusList.get(id_v)==1)	return true;
		return false;
	}
	public boolean areAdyacentVerts(int id_v1, int id_v2){
		int tam1 = this.AdjacencyList.get(id_v1).size();
		int tam2 = this.AdjacencyList.get(id_v2).size();
		if(tam1<tam2){
			return this.AdjacencyList.get(id_v1).contains(id_v2);
		}else{
			return this.AdjacencyList.get(id_v2).contains(id_v1);
		}
	}
	public int getFreeVertFewerNeighbors(){//Devuelve el indice del vertice LIBRE con MENOR cantidad de vecinos
		if(this.isMaximal()) return -1;
		//int minNeighbors = Integer.MAX_VALUE;
		int index_v=-1;
		if(tam_free==1){//si queda un solo vertice libre
			index_v = tam_sol;//se ubica en la primera posicion del 2°bloque
		}
		else{
			//if(this.tam_free>(this.nVert/2)){
			for (int i = 0; i < this.nVert; i++) {//Por cada vertice
				int id_v = NumberNeighborsList.get(i);
				if(StatusList.get(id_v)==0){
					return this.getIndexVert(id_v, 2);
				}
			}
		}
		return index_v; //TERMINA
	}
	public int getFreeVertMoreNeighbors(){//Devuelve el indice del vertice LIBRE con MAYOR cantidad de vecinos
		if(this.isMaximal()) return -1;
		//int maxNeighbors = -1;
		int index_v = -1;
		if(tam_free==1){//si queda un solo vertice libre
			index_v = tam_sol;//se ubica en la primera posicion del 2°bloque
		}
		else{
			for (int i = 0; i < this.nVert; i++) {//Por cada vertice
				int id_v = NumberNeighborsList.get(nVert-i-1);//de atras para delante
				if(StatusList.get(id_v)==0){
					return this.getIndexVert(id_v, 2);
				}
			}
		}
		return index_v; //TERMINA
	}
	public int getFreeVertFewerFreeNeighbors(){//Devuelve el indice del vertice LIBRE con MENOR cantidad de vecinos LIBRES
		if(this.isMaximal()) return -1;
		int minNeighbors = Integer.MAX_VALUE;
		int index_v=-1;
		if(tam_free==1){//si queda un solo vertice libre
			index_v = tam_sol;//se ubica en la primera posicion del 2°bloque
		}
		else{
			for (int i = this.tam_sol; i < (this.tam_free+this.tam_sol); i++) {//Por cada vertice libre
				int id_v = VertexList.get(i);//id del vertice de indice i
				int numNeighbors_Free = this.numNeighborsXTight(id_v, 0);//numero de vecinos LIBRES del vertice id_v
				//System.out.println(id_v);
				if(numNeighbors_Free < minNeighbors) {//Si el ítem actual tiene MENOR numero de vecinos libres
					minNeighbors = numNeighbors_Free;//Se guarda como el vertice con MENOR cantidad de vecinos LIBRES
					index_v = i;
				}
			}
		}
		return index_v; //TERMINA
	}
	public int getSolVertMoreNeighbors(){//Devuelve el indice del vertice SOLUCION con MAYOR cantidad de vecinos
		if(this.isVoid()) return -1;
		//int maxNeighbors = 0;
		int index_v=-1;
		if(tam_sol==1){//si queda un solo vertice sol
			index_v = 0;//se ubica en la primera posicion del 1°bloque
		}
		else{	
			for (int i = 0; i < this.nVert; i++) {//Por cada vertice
				int id_v = NumberNeighborsList.get(nVert-i-1);//de atras para delante
				if(StatusList.get(id_v)==1){
					return this.getIndexVert(id_v, 1);
				}
			}
			/*for (int i = 0; i < this.tam_sol; i++) {//Por cada vertice solucion
					int id_v = Sol.get(i);//id del vertice de indice i
					int numNeighbors = this.AdyaList.get(id_v).size();//numero de vecinos del vertice id_v
					if(numNeighbors > maxNeighbors) {//Si el ítem actual tiene mayor numero de vecinos
						maxNeighbors = numNeighbors;//Se guarda como el vertice con mayor cantidad de vecinos
						index_v = i;
					}
				}*/
		}
		return index_v; //TERMINA
	}
	public int getSolVertFewerNeighbors(){//Devuelve el indice del vertice SOLUCION con MENOR cantidad de vecinos
		if(this.isVoid()) return -1;
		//int minNeighbors = Integer.MAX_VALUE;
		int index_v=-1;
		if(tam_sol==1){//si queda un solo vertice sol
			index_v = 0;//se ubica en la primera posicion del 1°bloque
		}
		else{
			for (int i = 0; i < this.nVert; i++) {//Por cada vertice
				int id_v = NumberNeighborsList.get(i);//de atras para delante
				if(StatusList.get(id_v)==1){
					return this.getIndexVert(id_v, 1);
				}
			}
			/*for (int i = 0; i < this.tam_sol; i++) {//Por cada vertice solucion
					int id_v = Sol.get(i);//id del vertice de indice i
					int numNeighbors = this.AdyaList.get(id_v).size();//numero de vecinos del vertice id_v
					if(numNeighbors < minNeighbors) {//Si el ítem actual tiene menor numero de vecinos
						minNeighbors = numNeighbors;//Se guarda como el vertice con menor cantidad de vecinos
						index_v = i;
					}
				}*/
		}
		return index_v; //TERMINA
	}
	public int getSolVertMore1_TightNeighbors(){//Devuelve el indice del vertice SOLUCION con MAYOR cantidad de vecinos 1_Tight
		if(this.isVoid()) return -1;
		int maxNeighbors = 0;
		int index_v=0;
		if(tam_sol==1){//si queda un solo vertice sol
			index_v = 0;//se ubica en la primera posicion del 1°bloque
		}
		else{//existe mas de un vertice sol
			for (int i = 0; i < this.tam_sol; i++) {//Por cada vertice solucion
				int id_v = VertexList.get(i);//id del vertice de indice i
				int numNeighbors_1_Tight = this.numNeighborsXTight(id_v, 1);//numero de vecinos 1_tight del vertice id_v 
				if(numNeighbors_1_Tight > maxNeighbors) {//Si el ítem actual tiene mayor numero de vecinos
					maxNeighbors = numNeighbors_1_Tight;//Se guarda como el vertice con mayor cantidad de vecinos 1_tight
					index_v = i;
				}
			}
		}
		return index_v; //TERMINA
	}
	public int numNeighborsXTight(int id_index, int x_tight){//numero de vecinos X_tight del vertice id_index

		int num_neighbors = this.AdjacencyList.get(id_index).size();//numero de vecinos del vertice id_index
		//System.out.println(id_index+"   "+num_neighbors);
		int cont= 0;
		for(int i=0;i<num_neighbors;i++){//recorre todo los vertices vecinos del vertice id_index 
			int id_vert_neighbor = this.AdjacencyList.get(id_index).get(i);//id del vecino i del vertice id_index
			if(this.TightList.get(id_vert_neighbor)==x_tight){//si el tight del vertice vecino es x_tight
				cont++;//mas uno al contador de vertices que cumplen con X_Tigth
			}
		}
		return cont;//numero de vertices que cumple con X_Tight
	}
	public List<Integer> getNeighborsListXTight(int id_index, int x_tight){//Devuelve la lista de Vecinos del vertice id_index con tight X
		if(x_tight==0)System.out.println("Error: Existe otra metodo para los Tight=0!!");
		List<Integer> lista= new ArrayList<Integer>();
		int num_neighbors = this.AdjacencyList.get(id_index).size();//numero de vecinos del vertoce id_index
		for(int i=0;i<num_neighbors;i++){//recorre todo los vertices vecinos del vertice id_index 
			int id_vert_neighbor = this.AdjacencyList.get(id_index).get(i);//id del vecino i del vertice id_index
			if(this.TightList.get(id_vert_neighbor)==x_tight){//si el tight del vertice vecino es x_tight
				lista.add(id_vert_neighbor);//el vertice se agrega a la lista
			}
		}
		return lista;//lista con los vertices que cumple con X_Tight
	}
	public List<Integer> getNeighborsList_Sol(int id_index){//Devuelve la lista de Vecinos Sol del vertice id_index
		List<Integer> lista= new ArrayList<Integer>();
		int num_neighbors = this.AdjacencyList.get(id_index).size();//numero de vecinos del vertice id_index
		for(int i=0;i<num_neighbors;i++){//recorre todo los vertices vecinos del vertice id_index 
			int id_vert_neighbor = this.AdjacencyList.get(id_index).get(i);//id del vecino i del vertice id_index
			if(this.isSolVert(id_vert_neighbor))//si el vert vecino es un vertice solucion
				lista.add(id_vert_neighbor);//el vertice sol se agrega a la lista
		}
		return lista;//lista con los vertices solucion
	}
	public List<Integer> getNeighborsList_Free(int id_index){//Devuelve la lista de Vecinos Libres del vertice id_index
		List<Integer> lista= new ArrayList<Integer>();
		int num_neighbors = this.AdjacencyList.get(id_index).size();//numero de vecinos del vertice id_index
		for(int i=0;i<num_neighbors;i++){//recorre todo los vertices vecinos del vertice id_index 
			int id_vert_neighbor = this.AdjacencyList.get(id_index).get(i);//id del vecino i del vertice id_index
			if(this.isFreeVert(id_vert_neighbor))//si el vert vecino es un vertice libre
				lista.add(id_vert_neighbor);//el vertice libre se agrega a la lista
		}
		return lista;//lista con los vertices libres
	}
	public List<Integer> getVertList_XTight_noSol_noFree(int x_tight){//Devuelve la lista de TODOS los vertices no sol no libres con tight X
		List<Integer> lista= new ArrayList<Integer>();
		for(int i=this.tam_free+this.tam_sol;i<nVert;i++){//recorre todo los vertices no solucion no libres
			int id_vert = this.VertexList.get(i);//id del vertice no sol no libre
			if(this.TightList.get(id_vert)==x_tight){//si el tight del vertice es x_tight
				lista.add(id_vert);//el vertice se agrega a la lista
			}
		}
		return lista;//lista con los vertices que cumple con X_Tight
	}
	public Integer getFirstFreeNeighbor(int id_index){//Devuelve el primer vecino Libres del vertice id_index
		int num_neighbors = this.AdjacencyList.get(id_index).size();//numero de vecinos del vertice id_index
		for(int i=0;i<num_neighbors;i++){//recorre todo los vertices vecinos del vertice id_index 
			int id_vert_neighbor = this.AdjacencyList.get(id_index).get(i);//id del vecino i del vertice id_index
			if(this.isFreeVert(id_vert_neighbor))//si el vert vecino es un vertice libre
				return (id_vert_neighbor);//el vertice libre se agrega a la lista
		}
		return -1;//primer vecino libre
	}
	//VALIDERS
	public boolean Validar_IS(){
		for(int i=0; i<this.tam_sol;i++){
			int id_i = this.VertexList.get(i);
			for(int j=0;j<this.AdjacencyList.get(id_i).size();j++){
				for(int k=i+1;k<this.tam_sol;k++){
					int id_v = this.AdjacencyList.get(id_i).get(j);
					if(id_v==this.VertexList.get(k)){
						return false;
					}
				}
			}
		}
		return true;
	}
	public int Validar2_IS(){
		int acum=0;
		for(int i=this.tam_sol+this.tam_free; i<this.nVert;i++){
			int id_i = this.VertexList.get(i);
			boolean flag = false;
			for(int j=0;j<this.AdjacencyList.get(id_i).size();j++){				
				for(int k=0;k<this.tam_sol;k++){
					int id_v = this.AdjacencyList.get(id_i).get(j);
					if(id_v==this.VertexList.get(k)){
						flag = true;
						k=this.tam_sol;
						j=this.AdjacencyList.get(id_i).size();
					}
				}				
			}
			if(!flag){
				acum++;
			}
		}
		return acum;
	}
	//IMPROVEMENTS sobre Maximales
	public boolean Mejora_2_original(){

		//1ª Se procesa cada vertice x perteneciente a la sol
		List<Integer> solucion = new ArrayList<Integer>(VertexList.subList(0, tam_sol));//Se trabaja con un lista aparte mientras se modifica la solucion
		Print_Sol();//BORRAR
		for(int i=0;i<tam_sol;i++){//por cada vertices sol
			int id_x = solucion.get(i);//id del vertice sol i
			int index_x = this.getIndexVert(id_x, 1);

			//SE REMUEVE X DE LA SOLUCION
			removeVert(index_x);//SOLUCION S', sin el vertice x
			System.out.println("("+i+")REMOVE("+id_x+"): Vertice X solucion | Total vecinos libres: "+(tam_free-1));//BORRAR

			//if(!hasLess_X_Neighbors(id_x,2)){// si tiene menos de 2 VECINOS libres no existe mejora2 con x
			if(tam_free>=3){// si la sol tiene menos de 2 VERTICES libres NO EXISTE MEJORA_2 con x, 3 = ( 2 mas el nuevo vertice libre x) 

				//2ª Se procesa cada vecino libre del vertice x
				int num_neighbors = this.AdjacencyList.get(id_x).size();//numero de vecinos del vertice x
				for(int v=0;v<num_neighbors;v++){//recorre todos los vecino de x
					int id_v = getIdVertNeigh(id_x, v);//id del vecino con indice v, del vertice x
					if(isFreeVert(id_v)){//si el vertice v es libre

						//SE INSERTA EL VECINO LIBRE V EN LA SOLUCION S'
						System.out.print("	INSERT("+id_v+"): vecino V libre | ");//BORRAR
						int index_v = getIndexVert(id_v, 2);//indice del vertice libre V en la solucion
						insertVert(index_v);//SOLUCION S'',con el vecino V

						//3ª Se comprueba que la solucion contiene un vertice libre W						
						//if(is.hasLess_X_Neighbors(id_x,1)){//Si tiene menos de 1 VECINO libre no existe mejora2 con el vecino v
						System.out.print("Vertices W libres:"+tam_free+" | ");//BORRAR
						if(tam_free<1){//Si la sol tiene menos de 1 VERTICE libre no existe mejora2 con el vecino v
							System.out.println("REMOVE("+VertexList.get(tam_sol-1)+"): vecino V, no tiene vecino W libre");//BORRAR
							removeVert(tam_sol-1);//Se RESTAURA a S', sin el vecino v
						}
						else{//Al menos tiene un VERTICE libre W
							//SE INSERTA EL VECINO W EN LA SOLUCION S''						
							int index_w = tam_sol;//indice del vertice w es el primer vertice libre en la solucion
							System.out.println("("+i+")INSERT("+VertexList.get(index_w)+"): vecino W libre");//BORRAR
							insertVert(index_w);//NUEVA SOLUCION S,con el vecino W
							System.out.println("FIN: MEJORA-2 ENCONTRADA!");
							return true;//EXISTE MEJORA_2
						}
					}
				}
				index_x = this.getIndexVert(id_x, 2);
				System.out.println("("+i+")INSERT("+VertexList.get(index_x)+"): no existe Mejora con el Vertice X");
			}
			else {

				//el vertice x tomo la posicion del primer elemento de 2° bloque
				index_x = tam_sol;
				System.out.println("("+i+")INSERT("+VertexList.get(index_x)+"): X  tiene menos de dos vecinos libres");
			}
			//NO EXISTE MEJORA_2 con el vertice x
			insertVert(index_x);
		}
		System.out.println("FIN: NO hay Mejora!");
		return false;//NO EXISTE MEJORA_2 
	}
	public boolean Mejora_2_Maximal()//Version 2.0 de Mejora 2
{
		if(!this.isMaximal()){
			//System.out.println("  M2_Maximal: NO, la Sol NO es Maximal");
			return false;
		}
		if(this.isVoid()) {//Sol vacia, no es posible realizar una mejora
			//System.out.println("  M2_Maximal: NO, la solucion es vacía");
			return false;//NO EXISTE MEJORA_2 
		}
		//1ª Se procesa cada vertice x perteneciente a la sol
		List<Integer> solucion = new ArrayList<Integer>(VertexList.subList(0, tam_sol));//Se trabaja con un lista aparte mientras se modifica la solucion
		for(int i=0;i<tam_sol;i++){//por cada vertices sol
			int id_x = solucion.get(i);//id del vertice sol i
			//List<Integer> vecinos_x_tight_1 = new ArrayList<Integer>(this.getNeighborsListXTight(id_x,1));
			List<Integer> vecinos_x_tight_1 = this.getNeighborsListXTight(id_x,1);
			int neighbors_x = vecinos_x_tight_1.size();
			//El vertice X deber tener almenos dos vecinos tight 1, que no sean vecinos
			if(neighbors_x>=2) {				
				for(int v=0;v<neighbors_x-1;v++){//por cada vecino de x
					int neighbor_v = vecinos_x_tight_1.get(v);//vecino v
					for(int w=v+1;w<neighbors_x;w++){//por cada vecino x restante
						int neighbor_w = vecinos_x_tight_1.get(w);//vecino w
						if(!areAdyacentVerts(neighbor_v,neighbor_w)){//Si no son adyacentes
							//System.out.print("  M2_Maximal: SI,[X="+id_x+"|V="+neighbor_v+", W="+neighbor_w+"] ");
							this.removeVert(i);//vertice X
							int index_v = this.getIndexVert(neighbor_v, 2);
							this.insertVert(index_v);//vertice V
							int index_w = this.getIndexVert(neighbor_w, 2);
							this.insertVert(index_w);//vertice W
							//Si existen mas vertices libres, se deben Insertar
							if(this.isMaximal()){
								//System.out.print(" Maximal!: ");
								//Print_Cants();
							}else{
								//System.out.print("NO Maximal!: ");
								this.llenar_por_menor_num_vecinos_libres();
								//Print_Cants();
							}
							return true;
						}

					}
					//System.out.println("Vecino V=["+neighbor_v+"] ADYACENTE a los demas 1_Tight");
				}
				//System.out.println("NO con X=["+id_x+"]:no posee par {v,w} sin arista entre ellos");
			}
			else {
				//Si X tiene menos de dos vecinos 1_Tight, no esta involucrado en niguna Mejora2
				//System.out.println("NO con X=["+id_x+"]: menos de dos vecinos 1_tight");
			}			
		}
		//System.out.println("  M2_Maximal: NO, no existe");
		return false;//NO EXISTE MEJORA_2 
	}
	public boolean Mejora_3_Maximal(){
		if(!this.isMaximal()){
			//System.out.println("  M3_Maximal: NO, la Sol NO es Maximal");
			return false;
		}
		if(this.tam_sol<2) {//Sol vacia o con un elemento, no es posible realizar una mejora
			//System.out.println("  M3_Maximal: NO, la Sol posee menos de dos vertices.");
			return false;//NO EXISTE MEJORA_3
		}

		//SE PROCESA CADA VERTICE U 2_TIGHT, adyacente a los vertices solucion X e Y  
		List<Integer> List_U_2_tight = this.getVertList_XTight_noSol_noFree(2);
		for(int u=0;u<List_U_2_tight.size();u++){
			int id_u =  List_U_2_tight.get(u);
			List<Integer> solneighborsXY_of_U = this.getNeighborsList_Sol(id_u);//VECINOS SOLUCION DE U, X e Y
			int id_x = solneighborsXY_of_U.get(0);//id del vertice solucion X
			int id_y = solneighborsXY_of_U.get(1);//id del vertice solucion Y
			int index_x = this.getIndexVert(id_x, 1);//indice del vertice solucion X
			this.removeVert(index_x);//SE REMUEVE X, U se vuelve tight-1
			int index_y = this.getIndexVert(id_y, 1);//indice del vertice solucion Y
			this.removeVert(index_y);//SE REMUEVE Y, U se vuelve libre
			int index_u = this.getIndexVert(id_u, 2);//indice del vertice libre U
			this.insertVert(index_u);//SE INSERTA U, U se vuelve sol
			//SEA S' LA NUEVA SOLUCION
			if(this.tam_free>1){//Si tiene mas de 1 VertLIBRE
				//AlMENOS HAY DOS VERTICES LIBRES			
				List<Integer> freeNeighbors_ofX = this.getNeighborsList_Free(id_x);//VECINOS V LIBRES DE X
				List<Integer> freeNeighbors_ofY = this.getNeighborsList_Free(id_y);//VECINOS V LIBRES DE Y
				if(freeNeighbors_ofX.size()>0 && freeNeighbors_ofY.size()>0){//Si el vertice X/Y tienen almenos un vertice V/W libre
					//PARA CADA VECINO LIBRE V DE X				
					for(int v=0;v<freeNeighbors_ofX.size();v++){
						int id_v =  freeNeighbors_ofX.get(v);
						int index_v = this.getIndexVert(id_v, 2);//indice del vertice libre V
						////VECINOS LIBRES DE V
						this.insertVert(index_v);//SE INSERTA V
						//SE CREA S'' LA NUEVA SOLUCION
						if(!this.isMaximal()){//SI NO ES MAXIMAL, AGREGAR CUALQUIER VERTICE LIBRE W
							int index_w = this.tam_sol;
							//int id_w = Sol.get(index_w);
							this.insertVert(index_w);//SE INSERTA V
							//SOLUCION VALIDA
							//System.out.print("  M3_Maximal: SI,[X="+id_x+",Y="+id_y+"|U="+id_u+",V="+id_v+",W="+id_w+"]! ");
							//Si existen mas vertices libres, de deben Insertar
							if(this.isMaximal()){
								//System.out.print(" Maximal!: ");
								//Print_Cants();
							}else{
								//System.out.print(" NO es Maximal!: ");
								//this.llenar_por_menor_num_vecinos();
								this.llenar_por_menor_num_vecinos_libres();
								//Print_Cants();
							}													
							return true;
						}else{//S'' ES MAXIMAL, RETIRAR V E INTENTAR CON OTRO VECINO LIBRE V DE X
							index_v = this.getIndexVert(id_v, 1);//indice del vertice solucion V
							this.removeVert(index_v);//SE RETIRA V
						}
					}
				}				
			}
			index_u = this.getIndexVert(id_u, 1);//indice del vertice sol U
			this.removeVert(index_u);//SE REMUEVE U
			index_y = this.getIndexVert(id_y, 2);//indice del vertice libre Y
			this.insertVert(index_y);//SE INSERTA Y, U se vuelve 1-TIGHT
			index_x = this.getIndexVert(id_x, 2);//indice del vertice libre X
			this.insertVert(index_x);//SE INSERTA X, U se vuelve Tight-2

		}
		//System.out.println("  M3_Maximal: NO, no existe");
		return false;

	}
	public boolean Ms_2_Maximal(){
		boolean entra = false;
		while(Mejora_2_Maximal()){entra=true;}
		return entra;
	}
	public boolean Ms_3_Maximal(){
		boolean entra = false;
		while(Mejora_3_Maximal()){entra=true;}
		return entra;
	}
	public boolean M_2o3_Maximal(){
		if(Mejora_2_Maximal()||Mejora_3_Maximal())
			return true;
		return false;	
	}
	public boolean Ms_2y3_Maximal(){
		boolean mejora=false;
		boolean m2_listo=true;
		boolean m3_listo=false;
		while(m2_listo||m3_listo){//mientras esten pendientes
			//if(m2_listo!=true){//si aun no se raliza m2
			if(Ms_2_Maximal()){//si logra una m2 realizar m3
				m2_listo=true;
				mejora=true;
			//	}
			}
			else m2_listo=false;
			if(m2_listo==false){//si aun no se raliza m3
				if(Ms_3_Maximal()){//si logra una m3 realizar m2
					m3_listo=true;
					mejora=true;
				}
				else m3_listo=false;
			}		
		}
		return mejora;	
	}	//Terminal Meta-Heuristic
	public boolean Perturb(int k)//PERTURBAR
	{
		for(int i=0;i<k;i++){
			int index_vert_add = (int) Math.floor(Math.random()*(this.nVert-(this.tam_sol+this.tam_free))+(this.tam_sol+this.tam_free));
			int id_vert_add = VertexList.get(index_vert_add);
			List<Integer> neighbors_List = this.getNeighborsList_Sol(id_vert_add);
			for(int j=0;j<neighbors_List.size();j++){//remover cada vecino solucion
				int index_neighbor = this.getIndexVert(neighbors_List.get(j), 1); 
				this.removeVert(index_neighbor);
			}//luego de remover a todos los vecino solución,se debe buscar nuevamente el vertice a insertar, pero ahora se debe libre
			index_vert_add = this.getIndexVert(id_vert_add, 2);
			this.insertVert(index_vert_add);
		}
		this.llenar_por_menor_num_vecinos_libres();
		return true;
	}
	public void ILS(){
		int iter_cont=0;
		int max = ((2*this.mArist)/this.nVert);//grado de vertices
		this.original_sol = this.clone();
		for(int i=0;i<max;i++){
			IndependentSet new_sol = this.clone();//se clona la solución original
			new_sol.Perturb(1);//se perturba
			new_sol.Ms_2y3_Maximal();//se mejora
			if(new_sol.currentProfit >= this.currentProfit){//nueva solución mejor o igual que la sol original
				this.setBestSol(new_sol);
				iter_cont=0;
			}else{
				if(iter_cont>this.tam_sol){//si la solución no mejora en el tiempo, se permite ir a una peor solución
					if(this.currentProfit >= this.original_sol.currentProfit)//solo si es mejor que la ultima solución
						this.original_sol = this.clone();//salvamos la ultima mejor solución
					this.setBestSol(new_sol);
					iter_cont=0;
				}else
					iter_cont++;			
			}
		}
		//System.out.println(this.original_sol.currentProfit+" > "+this.currentProfit);
		if(this.original_sol.currentProfit>this.currentProfit){
			this.setBestSol(this.original_sol);
		}
		
	}
	//Terminales BUILDERS
	public void llenar_por_vertice_libre_aleatorio(){
		//System.out.println("LLENAR:[Vertice Aleatorio]");
		while(!isMaximal()){
			int indice_aleatorio = (int) Math.floor(Math.random()*(this.tam_free)+this.tam_sol);
			insertVert(indice_aleatorio);
		}
	}
	public void llenar_por_menor_num_vecinos(){
		//System.out.println("LLENAR: [Menor numero de Vecinos]");
		while(!isMaximal()){
			int indice = this.getFreeVertFewerNeighbors();
			insertVert(indice);
		}
	}
	public void llenar_por_mayor_num_vecinos(){
		//System.out.println("LLENAR:[Mayor numero de Vecinos]");
		while(!isMaximal()){
			int indice = this.getFreeVertMoreNeighbors();
			insertVert(indice);
		}
	}
	public void llenar_por_menor_num_vecinos_libres(){
		//System.out.println("LLENAR:[Menor numero de Vecinos LIBRES]");
		while(!isMaximal()){
			int indice = this.getFreeVertFewerFreeNeighbors();
			insertVert(indice);
		}
	}
	//PRINTERS
	public void Print_info_insert(int id){
		System.out.print("INSERT("+id+")");
		System.out.print(" VecinTotal("+this.AdjacencyList.get(id).size()+")");
		System.out.print(" VecinFree("+this.numNeighborsXTight(id,0)+")");
		System.out.println(" TotalFree("+this.tam_free+")");
	}
	public void Print_ListAdya(){
		for(int i=0; i< AdjacencyList.size();i++){
			System.out.println("["+i+"](tam:"+AdjacencyList.get(i).size()+"):("+AdjacencyList.get(i)+") ");
		}
	}
	public void Print_Cants(){
		int TNoSolNoFree = this.nVert-this.tam_sol-this.tam_free;
		System.out.println("TSol:"+this.tam_sol+" TFree:"+tam_free+" TNoSolNoFree:"+TNoSolNoFree);
	}
	public void Print_Sol(){
		System.out.println(VertexList.subList(0, tam_sol)+""+VertexList.subList(tam_sol,(tam_sol+tam_free))+""+VertexList.subList(tam_sol+tam_free, nVert));
	}
	public void Print_Graph(){
		for(int id=0; id< nVert;id++){
			if(isSolVert(id)){
				System.out.print("["+id+"]");
			}else if(isFreeVert(id)){
				System.out.print("("+id+")");
			}else
				System.out.print("<"+id+">");


		}
		System.out.println();
	}
	public void Print_InfoMISP(){
		Print_Cants();
		Print_Sol();
		Print_Graph();
	}

	public IndependentSet clone() {	
		IndependentSet is = new IndependentSet();
		is.currentProfit = this.currentProfit;
		is.mArist = this.mArist;
		is.nVert = this.nVert;
		is.tam_free = this.tam_free;
		is.tam_sol = this.tam_sol;
		is.AdjacencyList = this.AdjacencyList;
		is.VertexList = new ArrayList<Integer>(this.VertexList);
		is.TightList = new ArrayList<Integer>(this.TightList);
		//is.NumNeighborsList = new ArrayList<Integer>(this.NumNeighborsList);
		is.NumberNeighborsList = this.NumberNeighborsList;
		is.StatusList = new ArrayList<Integer>(this.StatusList);
		//is.original_sol = this.original_sol.clone();
		return is;

	}

}