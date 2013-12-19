/**
 * 
 */
package com.oneteam.framework.android.connection;

import java.io.InputStream;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import com.oneteam.framework.android.connection.status.ActionStatus;
import com.oneteam.framework.android.connection.status.LoginStatus;
import com.oneteam.framework.android.model.Session;
import com.oneteam.framework.android.net.Connection;

/**
 * @author islam
 * 
 */
public abstract class AbstractManager<T extends ActionStatus<?, ?>> {

	protected Session mSession;

	protected T mActionStatus;

	protected int mAuthorized = -1;

	protected int mBadRequest = -1;

	protected boolean executeConnection(Connection connection) {

		if (connection == null) {
			return false;
		}

		connection.connect();

		connection.hasResponse();

		StatusLine statusLine = connection.getStatus();

		int statusCode = -1;

		if (statusLine != null) {
			statusCode = statusLine.getStatusCode();
		}

		switch (statusCode) {

		case HttpStatus.SC_OK:

			setAuthorized(1);

			setHasBadRequest(-1);

			break;

		case HttpStatus.SC_BAD_REQUEST:

			setAuthorized(-1);

			setHasBadRequest(1);

			break;

		case HttpStatus.SC_UNAUTHORIZED:

			setAuthorized(0);

			setHasBadRequest(-1);

			break;

		default:
			return false;
		}

		// Parse the responded JSON, and check the validity of the
		// access token

		InputStream in = connection.getContent();

		T actionStatus = readResponse(in);

		if (actionStatus == null) {
			return false;
		}

		setActionStatus(actionStatus);

		if (actionStatus instanceof LoginStatus) {

			if (actionStatus.getStatus()) {

				setActiveSession(new Session((String) actionStatus.getResults()));

				return true;
			}

			return false;
		}

		handleActionStatus(actionStatus);

		return actionStatus.getStatus();
	}

	public boolean hasBadRequest() {
		return (mBadRequest == 1 ? true : false);
	}

	public boolean hasNoBadRequest() {
		return (mBadRequest == 0 ? true : false);
	}

	/**
	 * @param badRequest
	 *            the mBadRequest to set
	 */
	public void setHasBadRequest(int badRequest) {
		this.mBadRequest = badRequest;
	}

	public boolean isAuthorized() {
		return (mAuthorized == 1 ? true : false);
	}

	public boolean isNotAuthorized() {
		return (mAuthorized == 0 ? true : false);
	}

	/**
	 * @param authorized
	 *            the mIsAuthorized to set
	 */
	public void setAuthorized(int authorized) {
		this.mAuthorized = authorized;
	}

	protected abstract void handleActionStatus(T actionStatus);

	/**
	 * @param in
	 */
	protected abstract T readResponse(InputStream in);

	protected void setActiveSession(Session session) {
		mSession = session;
	}

	public Session getActiveSession() {
		return mSession;
	}

	protected void setActionStatus(T status) {
		mActionStatus = status;
	}

	public T getActionStatus() {
		return mActionStatus;
	}

}
