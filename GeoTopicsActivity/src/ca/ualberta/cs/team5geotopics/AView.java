package ca.ualberta.cs.team5geotopics;

/**
 * All Views implement this interface. This allows
 * for the view to update when the model changes.
 * 
 * reference: https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FView.java
 */
@SuppressWarnings("hiding")
public interface AView<AModel> {
	/**
	 * Anyone implementing this interface must provide an update methode to be called
	 * when a model changes.
	 * 
	 *  @param 	model	The model that has changed
	 */
	public void update(AModel model);
}
