package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

/**
 * This is a generic Model class. The other Model classes
 * will be subclassed from this one. 
 * reference: 
 * https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FModel.java
 */

@SuppressWarnings("rawtypes")
public class AModel<V extends AView>{
	// the list of Views that we will update when model changes
	private transient ArrayList<V> views;
	
	/**
	 * Returns the list of Views currently registerd with the model.
	 *
	 * @return An ArrayList of views.
	 */
	public ArrayList<V> getViews() {
		return views;
	}

	/**
	 * Basic Model Constructor
	 *
	 * @return A Model
	 */
	public AModel() {
		super();
        views = new ArrayList<V>();
    }
	
	/**
	 * Registers a View with the model
	 *
	 * @param  view  The view to register
	 */
	public void addView(V view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }
	
	/**
	 * Removes a view from being registered with this model.
	 *
	 */
	public void deleteView(V view) {
        views.remove(view);
    }
	
	/**
	 * Notifies all the views associated with this model that the model has changed.
	 * This version replies with the actual model to signify that the whole model has changed.
	 *
	 */
	@SuppressWarnings("unchecked")
	public void notifyViews() {
        for (V view : views) {
            view.update(this);
        }
    }
	
	/**
	 * Notifies all the the registered views that the supplied model has changed.
	 * 
	 *@param model	The model that has changed.
	 */
	@SuppressWarnings("unchecked")
	public void notifyViews(AModel model) {
        for (V view : views) {
            view.update(model);
        }
    }

}
