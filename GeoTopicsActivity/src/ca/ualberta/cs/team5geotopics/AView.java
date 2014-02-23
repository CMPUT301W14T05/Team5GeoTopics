package ca.ualberta.cs.team5geotopics;

/*
 * All View implement this interface. This allows
 * for the view to update when the model changes.
 */
public interface AView<AModel> {
	public void update(AModel model);
}
