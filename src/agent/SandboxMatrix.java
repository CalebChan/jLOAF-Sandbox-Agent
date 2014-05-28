package agent;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.atomic.MatrixCell;
import org.jLOAF.sim.SimilarityMetricStrategy;


public class SandboxMatrix extends ComplexInput{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int rows = 0;
	private int cols = 0;
	
	private static SimilarityMetricStrategy simMet;
	
	public SandboxMatrix(String name, int [][]matrix) {
		super(name);
		this.rows = matrix.length;
		this.cols = matrix[0].length;
		for(int ii = 0; ii < this.rows; ii++){
			for(int jj = 0; jj < this.cols; jj++){
				MatrixCell mc = new MatrixCell(ii+"-"+jj, new Feature(matrix[ii][jj]));
				this.add(mc);
			}
		}
	}
	
	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of deferred to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(SandboxMatrix.isClassStrategySet()){
			return SandboxMatrix.similarity(this, i);
		}else{
			return super.similarity(i);
		}
	}

	private static double similarity(Input complexInput, Input i) {
		return SandboxMatrix.simMet.similarity(complexInput, i);
	}

	public static boolean isClassStrategySet(){
		if(SandboxMatrix.simMet == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setClassSimilarityMetric(SimilarityMetricStrategy s){
		SandboxMatrix.simMet = s;
	}

}
