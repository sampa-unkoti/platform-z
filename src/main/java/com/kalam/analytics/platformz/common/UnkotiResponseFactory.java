package com.kalam.analytics.platformz.common;

public class UnkotiResponseFactory {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static <T> UnkotiApiResponse<T> createSuccessResponse(T data) {
		final UnkotiApiResponse<T> response = new UnkotiApiResponse<T>();
		response.setStatus(UnkotiApiResponse.Status.SUCCESS);
		response.setData(data);
		return response;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static <T> UnkotiApiResponse<T> createFailureResponse(String message) {
		final UnkotiApiResponse<T> response = new UnkotiApiResponse<T>();
		response.setStatus(UnkotiApiResponse.Status.FAILURE);
		response.setMessage(message);
		return response;
	}
}
