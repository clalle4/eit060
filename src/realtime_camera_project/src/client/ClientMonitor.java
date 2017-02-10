package client;

import java.util.LinkedList;

/**
 * This is a class that contains pictures from the different cameras. It is also
 * responsible for handling real time problems by having synchronized methods
 * for methods that is used by more than one thread.
 */

public class ClientMonitor {
	private LinkedList<Image> cam1Images;
	private LinkedList<Image> cam2Images;
	private LinkedList<ClientOutputThread> outputThreads;
	private int cameraMode;
	private String viewMode;
	private boolean motionDetected;
	private boolean synchronous;
	private String triggeringCamera;

	public static final int AUTO = 0;
	public static final int IDLE = 1;
	public static final int MOVIE = 2;

	public static final int SYNC_AUTO = 0;
	public static final int SYNC = 1;
	public static final int ASYNC = 2;

	private int syncMode = 0;
	private final int syncDelay = 5000;

	private final int tresHold = 200;
	private long latestTimeStampCam1 = 0;
	private long latestTimeStampCam2 = 0;

	/**
	 * Creates an instance of ClientMonitor. Sets the viewMode to auto at
	 * creation.
	 */
	// ta inte bort - kan anv�ndas f�r att visa delay i GUI
	private long delayCam1 = 0;
	private long delayCam2 = 0;

	public ClientMonitor() {
		cam1Images = new LinkedList<Image>();
		cam2Images = new LinkedList<Image>();
		outputThreads = new LinkedList<ClientOutputThread>();
		cameraMode = ClientMonitor.AUTO;
		syncMode = ClientMonitor.SYNC_AUTO;
		motionDetected = false;
		viewMode = "Auto";
	}

	/**
	 * Receives an image from either camera 1 or 2 depending on orderNbr, notes
	 * the time it was received from the server and saves it it camNImages,
	 * where N is the number from the camera the picture came from.
	 * 
	 * @param image
	 *            - The image to add
	 * @param orderNbr
	 *            - The camera number where the picture came from.
	 */

	public synchronized void addImage(Image image, int cameraNbr) {
		if (cameraNbr == 1) {
			cam1Images.add(image);
		} else if (cameraNbr == 2) {
			cam2Images.add(image);
		}
		notifyAll();
	}

	/**
	 * Sets the camera mode. If the set mode is MOVIE, then also sets
	 * motionDetected to true, else false.
	 * 
	 * @param mode
	 *            - An int that represent different camera modes.
	 */

	public synchronized void setCameraMode(int mode) {
		cameraMode = mode;
		if (cameraMode == MOVIE) {
			setMotionDetected(true, "Manual");
		} else {
			setMotionDetected(false, "Manual");
		}
		notifyAll();
	}

	public synchronized int getCameraMode() {
		return cameraMode;
	}

	public synchronized boolean motionDetected() {
		return motionDetected;
	}

	/**
	 * Changes motionDetected to true if motion was detected by a camera and
	 * saves which camera detected the motion. If motion was not detected by any
	 * camera, the attribute motionDeteced is set to false.
	 * 
	 * @param motionDetected
	 * @param triggeringCamera
	 */
	public synchronized void setMotionDetected(boolean motionDetected, String triggeringCamera) {
		if (!this.motionDetected && motionDetected) {
			this.triggeringCamera = triggeringCamera;
		}
		this.motionDetected = motionDetected;
		notifyAllOutputThreads();
		notifyAll();
	}

	/**
	 * Sets the syncMode and notifies all threads so the switch from movie/idle
	 * happens directly.
	 * 
	 * @param mode
	 *            - The string that determines what mode should be.
	 */
	public synchronized void setViewMode(String mode) {
		viewMode = mode;
		if (mode.equals("Auto"))
			syncMode = SYNC_AUTO;
		if (mode.equals("Synchronous")) {
			syncMode = SYNC;
		}
		if (mode.equals("Asynchronous")) {
			syncMode = ASYNC;
		}
		notifyAll();
	}

	public synchronized String getViewMode() {
		return viewMode;
	}

	public synchronized boolean synchronous() {
		return synchronous;
	}

	public synchronized String getTriggeringCamera() {
		return triggeringCamera;
	}

	/**
	 * Waits until there is atleast 1 image in the cam1Images list. When a
	 * picture is available, saves the timestamp and returns the image.
	 * 
	 * @return - The first image in list cam1Images
	 * @throws InterruptedException
	 *             if the method is abruptly aborted.
	 */
	public synchronized byte[] getCam1Image() throws InterruptedException {
		while (cam1Images.isEmpty()) {
			wait();
		}
		Image img = cam1Images.removeFirst();
		latestTimeStampCam1 = img.getTime();
		delayCam1 = System.currentTimeMillis() - img.getTime();

		setSynchronisedModeForImageFromCamera(1);
		if (synchronous) {
			while (System.currentTimeMillis() < img.getTime() + syncDelay) {
				System.out.println("cam 1: timeToSend = " + img.getTime() + syncDelay);
				wait();
			}
		}
		return img.getJPEG();
	}

	/**
	 * Waits until there is atleast 1 image in the cam2Images list. When a
	 * picture is available, saves the timestamp and returns the image.
	 * 
	 * @return - The first image in list cam2Images
	 * @throws InterruptedException
	 *             if the method is abruptly aborted.
	 */
	public synchronized byte[] getCam2Image() throws InterruptedException {
		while (cam2Images.isEmpty()) {
			wait();
		}
		Image img = cam2Images.removeFirst();

		// DELAY F�R ATT TESTA SYNCHRONIZED
		// img.addTime(-1000);

		latestTimeStampCam2 = img.getTime();
		delayCam2 = System.currentTimeMillis() - img.getTime();
		setSynchronisedModeForImageFromCamera(2);
		if (synchronous) {
			while (System.currentTimeMillis() < img.getTime() + syncDelay) {
				System.out.println("cam 1: timeToSend = " + (img.getTime() + syncDelay));
				wait();
			}
		}
		return img.getJPEG();
	}

	/**
	 * Switches the attribute synchronous depending on the intervalls between
	 * the pictures taken and also depending on the syncMode. Last is a
	 * notifyAll() call to notify the waiting threads that synchronous might
	 * have changed.
	 * 
	 * @param camera
	 *            - An int representing what camera the the timeStamp should be
	 *            taken from.
	 */
	private void setSynchronisedModeForImageFromCamera(int camera) {
		long currentTimeStamp = (camera == 1) ? latestTimeStampCam1 : latestTimeStampCam2;
		long prevTimeStamp = (camera == 1) ? latestTimeStampCam2 : latestTimeStampCam1;
		long nextImCam2Time = (!cam2Images.isEmpty()) ? cam2Images.getFirst().getTime() : -1;
		long nextImCam1Time = (!cam1Images.isEmpty()) ? cam1Images.getFirst().getTime() : -1;
		long nextTimeStamp = (camera == 1) ? nextImCam2Time : nextImCam1Time;

		switch (syncMode) {
		case SYNC_AUTO:
			if ((Math.abs(currentTimeStamp - prevTimeStamp) < tresHold)
					|| (nextTimeStamp != -1 && (Math.abs(currentTimeStamp - nextTimeStamp) < tresHold))) {
				synchronous = true;
			} else {
				synchronous = false;
			}
			break;
		case SYNC:
			synchronous = true;
			break;
		case ASYNC:
			synchronous = false;
			break;
		default:
			break;
		}
		notifyAll();
	}

	/**
	 * Adds an ClientOutputThread to the list outputThreads.
	 * 
	 * @param clientOutputThread
	 *            - The list that saves the added ClientOutputThread.
	 */
	public synchronized void addOutputThread(ClientOutputThread clientOutputThread) {
		outputThreads.add(clientOutputThread);
	}

	/**
	 * Notifies all outPutsThreads' waiting threads.
	 */
	private void notifyAllOutputThreads() {
		for (ClientOutputThread outputThread : outputThreads) {
			synchronized (outputThread) {
				outputThread.notifyAll();
			}
		}
	}
}
