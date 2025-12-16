package farn.another_alpha_again.sub;

import farn.another_alpha_again.Main;

import java.util.ArrayList;
import java.util.List;

public class QueueDispatcher {
	public static final List<Runnable> queue = new ArrayList<>();

	public static void runQueuedTasks() {
		if (!queue.isEmpty()) {
			for (Runnable r : new ArrayList<>(queue)) {
				r.run();
			}
			queue.clear();
		}
	}

	public static void queueChatMessage(String msg) {
		QueueDispatcher.queue.add(() -> {
			Main.mc.gui.addChatMessage(msg);
		});
	}
}
