package ca.ualberta.cs.team5geotopics;

/**
 * All Views implement this interface. This allows
 * for the view to update when the model changes.
 * 
 * reference: https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FView.java
 */
@SuppressWarnings("hiding")
public interface AView<AModel> {
	public void update(AModel model);
}
