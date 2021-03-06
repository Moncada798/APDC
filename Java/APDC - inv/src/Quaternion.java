import javax.swing.plaf.synth.SynthSeparatorUI;


public class Quaternion {
    private final double x0, x1, x2, x3; 

    public double getX0() {
		return x0;
	}

	public double getX1() {
		return x1;
	}

	public double getX2() {
		return x2;
	}

	public double getX3() {
		return x3;
	}

	// create a new object with the given components
    public Quaternion(double x0, double x1, double x2, double x3) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }
    
	 // return the quaternion norm
    public double norm() {
        return Math.sqrt(x0*x0 + x1*x1 +x2*x2 + x3*x3);
    }

    // return the quaternion conjugate
    public Quaternion conjugate() {
        return new Quaternion(x0, -x1, -x2, -x3);
    }
    
    // return a new Quaternion whose value is (this + b)
    public Quaternion plus(Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.x0+b.x0, a.x1+b.x1, a.x2+b.x2, a.x3+b.x3);
    }
    
 // return a new Quaternion whose value is (this * b)
    public Quaternion times(Quaternion b) {
        Quaternion a = this;
        double y0 = a.x0*b.x0 - a.x1*b.x1 - a.x2*b.x2 - a.x3*b.x3;
        double y1 = a.x0*b.x1 + a.x1*b.x0 + a.x2*b.x3 - a.x3*b.x2;
        double y2 = a.x0*b.x2 - a.x1*b.x3 + a.x2*b.x0 + a.x3*b.x1;
        double y3 = a.x0*b.x3 + a.x1*b.x2 - a.x2*b.x1 + a.x3*b.x0;
        //double y3 = a.x0*b.x3 + a.x3*b.x0 + a.x1*b.x2 - a.x2*b.x1;
        return new Quaternion(y0, y1, y2, y3);
    }

    // return a new Quaternion whose value is the inverse of this
    public Quaternion inverse() {
        double d = x0*x0 + x1*x1 + x2*x2 + x3*x3;
        return new Quaternion(x0/d, -x1/d, -x2/d, -x3/d);
    }

    // return a string representation of the invoking object
    public String toString() {
    	//(double)Math.round(value * 100000d) / 100000d
    	/*
    	 *  return (double)Math.round(x0* 100000d) / 100000d + " + " + (double)Math.round(x1* 100000d) / 100000d + "i + " 
    		+ (double)Math.round(x2* 100000d) / 100000d + "j + " + (double)Math.round(x3* 100000d) / 100000d + "k";
    	 */
        return x0 + " + " + x1 + "i + " + x2 + "j + " + x3 + "k";
    }
    
    public double [] getPoint(){
    	double [] p = new double [3];
    	p[0] = x1;
    	p[1] = x2;
    	p[2] = x3;
    	return p;
    }

}


class quaternionsOperations{
	
	public quaternionsOperations(){
		
	}
	
	//recebe um ponto, um vetor e o angulo (em graus) e calcula a transformação
	// p' = qpq'
	public Quaternion CalcQuaternion(double [] point, double [] vector, double angle){
		double angleRad = Math.toRadians(angle);
		Quaternion q = this.axisAngleToQuaternion(normalizeVetor(vector), angleRad);
		return this.timesWithVector(q, point);
	}
	
	public double [] normalizeVetor(double [] v){
		double [] norm = new double [3];
		double mag = 0;
		for (int i = 0; i< v.length; i++){
			mag+= v[i]*v[i];
		}
		if(Math.abs(mag) - 1.0 > 0.00001){
			double mag2 = Math.sqrt(mag);
			for (int i = 0; i< v.length; i++){
				norm[i] = v[i]/mag2;
				//System.out.print(norm[i] + " ");
			}
		}
		else
			norm = v;
		
		
		//System.out.println("");
		return norm;
	}
	
	//transforma de vetor angulo para quaternion
	public Quaternion axisAngleToQuaternion (double [] vector, double theta){
		double w = Math.cos(theta/2);
		double x = vector[0] * Math.sin(theta/2);
		double y = vector[1] * Math.sin(theta/2);
		double z = vector[2] * Math.sin(theta/2);
		return new Quaternion (w,x,y,z);
	}

    //perfom the aritmetics of p' = qpq'
    //when p is a point, or a 'pure' quaternion (w=0)
    public Quaternion timesWithVector(Quaternion q, double [] point){
    	Quaternion p = new Quaternion (0.0, point[0], point[1], point[2]);
    	return timesWithQuaternion(q, p);
    }
    
    public Quaternion timesWithQuaternion(Quaternion q1, Quaternion q2){
    	Quaternion qq = q1.times(q2);
    	//System.out.println(qq.toString());
    	Quaternion inv = q1.conjugate();
    	//System.out.println(inv.toString());
    	return qq.times(inv);
    }
    
    
    public Quaternion generateRandomQ(){
    	double u1 = Math.random();
    	double u2 = Math.random();
    	double u3 = Math.random();
    	
    	double w = Math.sqrt(1-u1)*Math.sin(2*Math.PI * u2);
    	double x = Math.sqrt(1-u1)*Math.cos(2*Math.PI * u2);
    	double y = Math.sqrt(u1)*Math.sin(2*Math.PI * u3);
    	double z = Math.sqrt(u1)*Math.cos(2*Math.PI * u3);
    	return new Quaternion (w,x,y,z);
    }
    
    
    

	
	
}
