package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import es.uvigo.ei.aibench.core.operation.annotation.ProgressProperty;

/**
 * @author ODias
 *
 */
public class TimeLeftProgress {

	private String time;
	private String message;
    private float total=0.0f;
    
    /**
	 * 
	 */
	public TimeLeftProgress() {

		this.time=new String();
	}


	@ProgressProperty(order = 1, label = "task: ")
	public String getTask() {
		return message;
	}
	
	/**
	 * @return
	 */
	@ProgressProperty(order = 2, label = "time left: ")
	public String getTime() {

		return time;
	}

	@ProgressProperty(order = 3, label = "total progress: ", showProgressBarLabel = true)
	public float getProgress() {
		return this.total;
	}

	/**
	 * @param differTime
	 * @param pos
	 * @param max
	 */
	public void setTime(long differTime, int pos, int max) {

		String timeLeft = new String();

		if(pos!=0) {

			float seconds = differTime/(float)1000;
			float eachSeconds = seconds/(float)pos;
			float timeL = eachSeconds*(max-pos); 
			int minutes = (int)timeL/60;
			this.total=((float)pos/(float)max);
			//int percentage = Math.round(((float)pos/(float)max)*100);

			if(minutes>59)				
				minutes = minutes % 60;

			int hour = (int) timeL/3600 ;
			//			System.out.println("pos :"+(float)pos);
			//			System.out.println("max :"+(float)max+"\n");
			//			System.out.println("progress :"+((float)pos/(float)max)+"\n");
			//			System.out.println("progress :"+((float)pos/(float)max)*100+"\n");
			//			System.out.println("timeRunSeconds :"+seconds);
			//			System.out.println("eachSeconds :"+eachSeconds);
			//			System.out.println("timeL :"+timeL);
			//			System.out.println("minutes :"+minutes);
			//			System.out.println("hour :"+hour);
			//System.out.println(this.getProgress());

			if(minutes==0 && hour==0) {

//				timeLeft = timeLeft.concat("Time Left : <1m");
				timeLeft = timeLeft.concat("<1m");
			}
			else {

//				timeLeft = timeLeft.concat(percentage+"%   Time Left : "+hour+"h "+minutes+"m");
				timeLeft = timeLeft.concat(hour+"h :"+minutes+"m");
			}
		}
		else {

			//timeLeft = timeLeft.concat("Time Left : oo");
			timeLeft = timeLeft.concat("oo");
		}
		this.time=timeLeft;
	}

	/**
	 * @param differTime
	 * @param pos
	 * @param max
	 * @param message
	 */
	public void setTime(long differTime, int pos, int max, String message) {

		message = message.trim();
		if(!message.isEmpty())
			message+=" ";

		this.message = (message);

		String timeLeft = new String();

		if(pos!=0) {

			float seconds = differTime/(float)1000;
			float eachSeconds = seconds/(float)pos;
			float timeL = eachSeconds*(max-pos); 
			int minutes = (int)timeL/60;
			this.total=(((float)pos/(float)max));
		//	int percentage = Math.round(((float)pos/(float)max)*100);

			if(minutes>59)				
				minutes = minutes % 60;

			int hour = (int) timeL/3600 ;
			//			System.out.println("pos :"+(float)pos);
			//			System.out.println("max :"+(float)max+"\n");
			//			System.out.println("progress :"+((float)pos/(float)max)+"\n");
			//			System.out.println("progress :"+((float)pos/(float)max)*100+"\n");
			//			System.out.println("timeRunSeconds :"+seconds);
			//			System.out.println("eachSeconds :"+eachSeconds);
			//			System.out.println("timeL :"+timeL);
			//			System.out.println("minutes :"+minutes);
			//			System.out.println("hour :"+hour);
			//System.out.println(this.getProgress());
		
			if(minutes==0 && hour==0) {

//				timeLeft = timeLeft.concat("Time Left : <1m");
				timeLeft = timeLeft.concat("<1m");
			}
			else {

//				timeLeft = timeLeft.concat(percentage+"%   Time Left : "+hour+"h "+minutes+"m");
				timeLeft = timeLeft.concat(hour+"h :"+minutes+"m");
			}
		}
		else {

			//timeLeft = timeLeft.concat("Time Left : oo");
			timeLeft = timeLeft.concat("oo");
		}
		this.time=timeLeft;
	}

}
