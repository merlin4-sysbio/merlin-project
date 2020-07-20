package pt.uminho.ceb.biosystems.merlin.biocomponents.container;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

public class ContainerUtils extends pt.uminho.ceb.biosystems.mew.biocomponents.container.ContainerUtils {

	public static boolean isReactionDrain(ReactionCI reaction) {

		int numProducts = reaction.getProducts().size();
		int numReactants = reaction.getReactants().size();

		if (numProducts == 0 || numReactants == 0)
			return true;
		return false;
	}
}
