package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

/*
 * This is a generic Model class. The other Model classes
 * will be subclassed from this one. 
 * reference: 
 * https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FModel.java
 */

public class AModel<V extends AView>{
	// the list of Views that we will update when model changes
	private transient ArrayList<V> views;
	
	// just in case
	public ArrayList<V> getViews() {
		return views;
	}

	public AModel() {
		super();
        views = new ArrayList<V>();
    }
	
	public void addView(V view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }
	
	public void deleteView(V view) {
        views.remove(view);
    }
	
	public void notifyViews() {
        for (V view : views) {
            view.update(this);
        }
    }
	

}
