package ca.ualberta.cs.team5geotopics;

/*
 * All View implement this interface. This allows
 * for the view to update when the model changes.
 */
public interface AView<M> {
	public void update(M model);
}
