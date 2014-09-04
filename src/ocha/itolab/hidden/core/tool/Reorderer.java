package ocha.itolab.hidden.core.tool;
import java.util.ArrayList;
import java.util.Random;

public class Reorderer
{
	private static int PARTICLE_COUNT = 100;
	private static int V_MAX = 4; // Maximum velocity change allowed.
										// Range: 0 >= V_MAX < CITY_COUNT

	private static int MAX_EPOCHS = 1000;

	private static ArrayList<Particle> particles;
	
	private static ArrayList<cCity> map;
	private static int CITY_COUNT = 8;
	//private static final double TARGET = 86.63;				// Number for algorithm to find.
	
	
	public static int[] reorder(double distance[][])
	{
		int[] newOrder = new int[distance.length];
		particles = new ArrayList<Particle>();
		map = new ArrayList<cCity>();
		 
		// copy the input distance information
		CITY_COUNT = distance.length;
		for(int i = 0; i < CITY_COUNT; i++){
	        cCity city = new cCity();
	        city.setId(i);
	        city.setNum(CITY_COUNT);
	        for(int j = 0; j < CITY_COUNT; j++)
	        	city.setDisSim(distance[i][j], j);
	        map.add(city);
	    }
	    
		// find the best solution
		PSOAlgorithm();
		
		// copy the result to the output array
		for(int j = 0; j < CITY_COUNT; j++)
            newOrder[j] = particles.get(0).data(j);
       
		return newOrder;
	}
	
	private static void PSOAlgorithm()
	{
		Particle aParticle = null;
		int epoch = 0;
		boolean done = false;

	    initialize();

	    while(!done)
	    {
	        // Two conditions can end this loop:
	        //    if the maximum number of epochs allowed has been reached, or,
	        //    if the Target value has been found.
	        if(epoch < MAX_EPOCHS){

	            for(int i = 0; i < PARTICLE_COUNT; i++)
	            {
	                aParticle = particles.get(i);
	                getTotalDistance(i);
	                
	                /*
	                System.out.print("Route: ");
	                for(int j = 0; j < CITY_COUNT; j++)
	                {
	                    System.out.print(aParticle.data(j) + ", ");
	                } // j
	                System.out.print("Distance: " + aParticle.pBest() + "\n");
	                if(aParticle.pBest() <= TARGET){
	                    done = true;
	                }
	                */
	            } // i
	            
	            bubbleSort(); // sort particles by their pBest scores, best to worst.
	            
	            getVelocity();

	            updateparticles();
	            
	            //System.out.println("epoch number: " + epoch);

	            epoch++;

	        }else{
	            done = true;
	        }
		}
	    return;
	}
	
	private static void initialize()
	{
		for(int i = 0; i < PARTICLE_COUNT; i++)
	    {
	        Particle newParticle = new Particle();
	        for(int j = 0; j < CITY_COUNT; j++)
	        {
	            newParticle.data(j, j);
	        } // j
	        particles.add(newParticle);
	        for(int j = 0; j < 10; j++)
	        {
	        	randomlyArrange(particles.indexOf(newParticle));
	        }
	        getTotalDistance(particles.indexOf(newParticle));
	    } // i
	    return;
	}
	
	private static void randomlyArrange(final int index)
	{
		int cityA = new Random().nextInt(CITY_COUNT);
		int cityB = 0;
		boolean done = false;
		while(!done)
		{
			cityB = new Random().nextInt(CITY_COUNT);
			if(cityB != cityA){
				done = true;
			}
		}
		
		int temp = particles.get(index).data(cityA);
		particles.get(index).data(cityA, particles.get(index).data(cityB));
		particles.get(index).data(cityB, temp);
		return;
	}
	
	private static void getVelocity()
	{
		double worstResults = 0;
		double vValue = 0.0;
		
		// after sorting, worst will be last in list.
	    worstResults = particles.get(PARTICLE_COUNT - 1).pBest();

	    for(int i = 0; i < PARTICLE_COUNT; i++)
	    {
	        vValue = (V_MAX * particles.get(i).pBest()) / worstResults;

	        if(vValue > V_MAX){
	        	particles.get(i).velocity(V_MAX);
	        }else if(vValue < 0.0){
	        	particles.get(i).velocity(0.0);
	        }else{
	        	particles.get(i).velocity(vValue);
	        }
	    }
	    return;
	}
	
	private static void updateparticles()
	{
		// Best is at index 0, so start from the second best.
	    for(int i = 1; i < PARTICLE_COUNT; i++)
	    {
    		// The higher the velocity score, the more changes it will need.
	    	int changes = (int)Math.floor(Math.abs(particles.get(i).velocity()));
    		//System.out.println("Changes for particle " + i + ": " + changes);
        	for(int j = 0; j < changes; j++){
        		if(new Random().nextBoolean()){
        			randomlyArrange(i);
        		}
        		// Push it closer to it's best neighbor.
        		copyFromParticle(i - 1, i);
        	} // j
	        
	        // Update pBest value.
	        getTotalDistance(i);
	    } // i
	    
	    return;
	}
	
	private static void printBestSolution()
	{
		/*
		if(particles.get(0).pBest() <= TARGET){
			// Print it.
			System.out.println("Target reached.");
		}else{
			System.out.println("Target not reached");
		}
		*/
		System.out.print("Shortest Route: ");
		for(int j = 0; j < CITY_COUNT; j++)
        {
            System.out.print(particles.get(0).data(j) + ", ");
        } // j
        System.out.print("Distance: " + particles.get(0).pBest() + "\n");
		return;
	}
	
	private static void copyFromParticle(final int source, final int destination)
	{
		// push destination's data points closer to source's data points.
		Particle best = particles.get(source);
		int targetA = new Random().nextInt(CITY_COUNT); // source's city to target.
		int targetB = 0;
		int indexA = 0;
		int indexB = 0;
		int tempIndex = 0;
		
		// targetB will be source's neighbor immediately succeeding targetA (circular).
		int i = 0;
		for(; i < CITY_COUNT; i++)
		{
			if(best.data(i) == targetA){
				if(i == CITY_COUNT - 1){
					targetB = best.data(0); // if end of array, take from beginning.
				}else{
					targetB = best.data(i + 1);
				}
				break;
			}
		}
		
		// Move targetB next to targetA by switching values.
		for(int j = 0; j < CITY_COUNT; j++)
		{
			if(particles.get(destination).data(j) == targetA){
				indexA = j;
			}
			if(particles.get(destination).data(j) == targetB){
				indexB = j;
			}
		}
		// get temp index succeeding indexA.
		if(indexA == CITY_COUNT - 1){
			tempIndex = 0;
		}else{
			tempIndex = indexA + 1;
		}
		
		// Switch indexB value with tempIndex value.
		int temp = particles.get(destination).data(tempIndex);
		particles.get(destination).data(tempIndex, particles.get(destination).data(indexB));
		particles.get(destination).data(indexB, temp);
		
		return;
	}
	
	private static void getTotalDistance(final int index)
	{
		Particle thisParticle = null;
	    thisParticle = particles.get(index);
	    thisParticle.pBest(0.0);
	    
	    for(int i = 0; i < CITY_COUNT; i++)
	    {
	        if(i == CITY_COUNT - 1){
	        	thisParticle.pBest(thisParticle.pBest() + getDistance(thisParticle.data(CITY_COUNT - 1), thisParticle.data(0))); // Complete trip.
	        }else{
	        	thisParticle.pBest(thisParticle.pBest() + getDistance(thisParticle.data(i), thisParticle.data(i + 1)));
	        }
	    }
	    return;
	}

	private static double getDistance(final int firstCity, final int secondCity)
	{
		cCity cityA = null;
		cCity cityB = null;
		double a2 = 0;
		double b2 = 0;
	    cityA = map.get(firstCity);
	    cityB = map.get(secondCity);
	    double dist = cityA.getDisSim(cityB.getId());

	    return Math.sqrt(a2 + b2);
	}
	
	private static void bubbleSort()
	{
		boolean done = false;
		while(!done)
		{
			int changes = 0;
			int listSize = particles.size();
			for(int i = 0; i < listSize - 1; i++)
			{
				if(particles.get(i).compareTo(particles.get(i + 1)) == 1){
					Particle temp = particles.get(i);
					particles.set(i, particles.get(i + 1));
					particles.set(i + 1, temp);
					changes++;
				}
			}
			if(changes == 0){
				done = true;
			}
		}
		return;
	}
	
	private static class Particle implements Comparable<Particle>
    {
	    private int mData[] = new int[CITY_COUNT];
	    private double mpBest = 0;
	    private double mVelocity = 0.0;
	
	    public Particle()
	    {
	        this.mpBest = 0;
	        this.mVelocity = 0.0;
	    }
	    
	    public int compareTo(Particle that)
	    {
	    	if(this.pBest() < that.pBest()){
	    		return -1;
	    	}else if(this.pBest() > that.pBest()){
	    		return 1;
	    	}else{
	    		return 0;
	    	}
	    }
	
	    public int data(final int index)
	    {
	    	return this.mData[index];
	    }
	    
	    public void data(final int index, final int value)
	    {
	        this.mData[index] = value;
	        return;
	    }
	
	    public double pBest()
	    {
	    	return this.mpBest;
	    }

	    public void pBest(final double value)
	    {
	    	this.mpBest = value;
	    	return;
	    }
	
	    public double velocity()
	    {
	    	return this.mVelocity;
	    }
	    
	    public void velocity(final double velocityScore)
	    {
	       this.mVelocity = velocityScore;
	       return;
	    }
    } // Particle
	

	private static class cCity
	{
		private int id;
		private double dissim[];
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public void setNum(int num) {
			dissim = new double[num];
		}
		
		public void setDisSim(double d, int id) {
			dissim[id] = d;
		}
		
		public double getDisSim(int id) {
			return dissim[id];
		}
		
	} // cCity
	
	/*
		initializeMap();
		PSOAlgorithm();
		printBestSolution();
		return;*/
	

}