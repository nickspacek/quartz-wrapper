package spacek.quartz;

import org.quartz.JobExecutionException;

public class GenericJobException extends JobExecutionException {

    /**
     * 
     */
    private static final long serialVersionUID = -4888176286081775415L;

    public GenericJobException() {
	// TODO Auto-generated constructor stub
    }

    public GenericJobException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public GenericJobException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public GenericJobException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}
