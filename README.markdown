Quartz Wrapper
==============

Description
-----------

This project is aimed at bringing easy scheduling and recovery of jobs to Java,
where a "job" is any task performed through an external API (such as Twitter or
Facebook). The idea is that we generate a wrapper class around the external API
and intercept method calls, wrapping them in a Quartz Job and executing them
there.

Syntax
------

Inspired by Mockito, the syntax I originally had in mind is something like this:

	SampleApi api = wrap(SampleApi.class);
	queue(api.postStatus("Howdy!"))
		.schedule(tomorrow)
		.callback(new Callback<Post>() {
			@Override
			public void run() {
				log.info("Posted '" + getResult().message + "'!");
			}
		});
  
Future
------

*	Persist results

	In the past I have written Quartz listeners to store results from job
	execution in a DB, to allow the Scheduler to be queried later for those
	results and persisting them even after restarts.

*	Custom error handling

	Adding the ability to retry jobs until they succeed (or get cancelled).
	
*	Custom setup of services/APIs

	Often the external API will require some configuration, so we should have
	some way to hook that in.

*	Other stuff...