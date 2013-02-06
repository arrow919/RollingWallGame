package com.studios927.rollingwallgame;

import com.studios927.rollingwallgame.ui.HorizontalScrollView;

public class Scroller extends Thread {
	private boolean running;
	//private int scrollTo;
	private HorizontalScrollView actOn;
	private boolean scrolling;

	public Scroller(HorizontalScrollView actOn) {
		this.actOn = actOn;
		running = true;
	}

	@Override
	public void run() {
		doWork();
	}

	private void doWork() {
		while (running) {
			while (actOn.getCurrentOffset() == actOn.getTargetOffset()) {
				synchronized (this) {
					try {
						scrolling = false;
						wait();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}

			}
			moveLoop();

		}
	}

	private void moveLoop() {
		scrolling = true;
		double currentOffset = actOn.getCurrentOffset();
		double targetOffset = actOn.getTargetOffset();
		double difference = Math.abs(currentOffset - targetOffset);
		if (currentOffset > targetOffset) {
			if (difference < 2) {
				actOn.setCurrentOffset(currentOffset - difference);
			} else {
				actOn.setCurrentOffset(currentOffset - 2);
			}
		} else if (currentOffset < targetOffset) {
			if (difference < 1) {
				actOn.setCurrentOffset(currentOffset + difference);
			} else {
				actOn.setCurrentOffset(currentOffset + 2);
			}
		}

		try {
			sleep(1);
		} catch (InterruptedException e) {
			
			e.printStackTrace();

		}
	}

	public synchronized void kill() {
		running = false;
		scrolling = false;
	}

	public boolean isScrolling() {
		return scrolling;
	}
}