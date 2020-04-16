package com.actlem.bike.generator;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Flow;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Service to read the {@link String} content of {@link BodyPublisher}
 */
public class StringBodyPublisherReader implements Flow.Subscriber<ByteBuffer> {

  private BodySubscriber<String> bodySubscriber;

  public StringBodyPublisherReader(BodyPublisher bodyPublisher) {
    bodySubscriber = BodySubscribers.ofString(UTF_8);
    bodyPublisher.subscribe(this);
  }

  public String getContent() {
    return bodySubscriber.getBody().toCompletableFuture().join();
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    bodySubscriber.onSubscribe(subscription);
  }

  @Override
  public void onNext(ByteBuffer item) {
    bodySubscriber.onNext(List.of(item));
  }

  @Override
  public void onError(Throwable throwable) {
    bodySubscriber.onError(throwable);
  }

  @Override
  public void onComplete() {
    bodySubscriber.onComplete();
  }
}
