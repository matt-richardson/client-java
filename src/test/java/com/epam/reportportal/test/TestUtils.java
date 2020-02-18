package com.epam.reportportal.test;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.restendpoint.http.MultiPartRequest;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.ta.reportportal.ws.model.BatchElementCreatedRS;
import com.epam.ta.reportportal.ws.model.BatchSaveOperatingRS;
import com.epam.ta.reportportal.ws.model.EntryCreatedAsyncRS;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.item.ItemCreatedRS;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRS;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import io.reactivex.Maybe;
import org.mockito.stubbing.Answer;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TestUtils {

	private TestUtils() {
	}

	public static Maybe<StartLaunchRS> startLaunchResponse(String id) {
		final StartLaunchRS rs = new StartLaunchRS();
		rs.setId(id);
		return getConstantMaybe(rs);
	}

	private static <T> Maybe<T> getConstantMaybe(final T rs) {
		return Maybe.create(emitter -> {
			emitter.onSuccess(rs);
			emitter.onComplete();
		});
	}

	public static void simulateStartLaunchResponse(final ReportPortalClient client) {
		when(client.startLaunch(any(StartLaunchRQ.class))).then((Answer<Maybe<StartLaunchRS>>) invocation -> {
			StartLaunchRQ rq = invocation.getArgument(0);
			return startLaunchResponse(ofNullable(rq.getUuid()).orElseGet(() -> UUID.randomUUID().toString()));
		});
	}

	public static StartLaunchRQ standardLaunchRequest(final ListenerParameters params) {
		StartLaunchRQ rq = new StartLaunchRQ();
		rq.setName(params.getLaunchName());
		rq.setStartTime(Calendar.getInstance().getTime());
		rq.setAttributes(params.getAttributes());
		rq.setMode(params.getLaunchRunningMode());
		rq.setRerun(params.isRerun());
		rq.setStartTime(Calendar.getInstance().getTime());
		return rq;
	}

	public static Maybe<ItemCreatedRS> startTestItemResponse(String id) {
		final ItemCreatedRS rs = new ItemCreatedRS();
		rs.setId(id);
		return getConstantMaybe(rs);
	}

	public static void simulateStartTestItemResponse(final ReportPortalClient client) {
		when(client.startTestItem(any(StartTestItemRQ.class))).then((Answer<Maybe<ItemCreatedRS>>) invocation -> {
			StartTestItemRQ rq = invocation.getArgument(0);
			return startTestItemResponse(ofNullable(rq.getUuid()).orElseGet(() -> UUID.randomUUID().toString()));
		});
	}

	public static void simulateStartChildTestItemResponse(final ReportPortalClient client) {
		when(client.startTestItem(anyString(), any(StartTestItemRQ.class))).then((Answer<Maybe<ItemCreatedRS>>) invocation -> {
			StartTestItemRQ rq = invocation.getArgument(1);
			return startTestItemResponse(ofNullable(rq.getUuid()).orElseGet(() -> UUID.randomUUID().toString()));
		});
	}

	public static StartTestItemRQ standardStartSuiteRequest() {
		StartTestItemRQ rq = new StartTestItemRQ();
		rq.setStartTime(Calendar.getInstance().getTime());
		String id = System.currentTimeMillis() + "-" + Thread.currentThread().getId() + "-" + ThreadLocalRandom.current().nextInt(9999);
		rq.setName("Suite_" + id);
		rq.setDescription("Suite description");
		rq.setUniqueId(id);
		rq.setType("SUITE");
		return rq;
	}

	public static StartTestItemRQ standardStartTestRequest() {
		StartTestItemRQ rq = new StartTestItemRQ();
		rq.setStartTime(Calendar.getInstance().getTime());
		String id = System.currentTimeMillis() + "-" + Thread.currentThread().getId() + "-" + ThreadLocalRandom.current().nextInt(9999);
		rq.setName("Test_" + id);
		rq.setDescription("Test description");
		rq.setUniqueId(id);
		rq.setType("TEST");
		return rq;
	}

	public static Maybe<EntryCreatedAsyncRS> logResponse(String id) {
		final EntryCreatedAsyncRS rs = new EntryCreatedAsyncRS();
		rs.setId(id);
		return getConstantMaybe(rs);
	}

	public static void simulateLogResponse(final ReportPortalClient client) {
		when(client.log(any(SaveLogRQ.class))).then((Answer<Maybe<EntryCreatedAsyncRS>>) invocation -> {
			StartLaunchRQ rq = invocation.getArgument(0);
			return logResponse(ofNullable(rq.getUuid()).orElseGet(() -> UUID.randomUUID().toString()));
		});
	}

	public static Maybe<BatchSaveOperatingRS> batchLogResponse(List<String> ids) {
		final BatchSaveOperatingRS rs = new BatchSaveOperatingRS();
		ids.forEach(i -> rs.addResponse(new BatchElementCreatedRS(i)));
		return getConstantMaybe(rs);
	}

	public static void simulateBatchLogResponse(final ReportPortalClient client) {
		when(client.log(any(MultiPartRequest.class))).then((Answer<Maybe<BatchSaveOperatingRS>>) invocation -> {
			MultiPartRequest rq = invocation.getArgument(0);
			List<String> saveRqs = rq.getSerializedRQs()
					.stream()
					.map(r -> (List<SaveLogRQ>) r.getRequest())
					.flatMap(Collection::stream)
					.map(s -> ofNullable(s.getUuid()).orElseGet(() -> UUID.randomUUID().toString()))
					.collect(Collectors.toList());
			return batchLogResponse(saveRqs);
		});
	}

}
