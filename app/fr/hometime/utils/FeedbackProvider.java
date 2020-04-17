package fr.hometime.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import models.Feedback;

public interface FeedbackProvider extends DataProvider {
	public Optional<List<Feedback>>retrieveFeedbacks();
	
	public default Optional<List<Feedback>> retrieveFeedbacksOrderedByDateAsc() {
		return Hidden.getFeedbacksOrderedBy(Comparator.comparing(Feedback::getDisplayDate).reversed(), retrieveFeedbacks());
	}
	
	public default Optional<List<Feedback>> retrieveFeedbacksThatShouldBeDisplayedOrderedByDateAsc() {
		return Hidden.getFeedbacksFilteredAndOrderedBy(Feedback::shouldDisplay, Comparator.comparing(Feedback::getDisplayDate).reversed(), retrieveFeedbacks());
	}
	
	public default Optional<List<Feedback>> retrieveFeedbacksOrderedByDateDesc() {
		return Hidden.getFeedbacksOrderedBy(Comparator.comparing(Feedback::getDisplayDate), retrieveFeedbacks());
	}
	
	public default Optional<List<Feedback>> retrieveRandomSubsetOfEmphasizedFeedbacks(int numberToRetrieve) {
		return Hidden.getRandomlyNFeedbacks(Feedback::mayBeEmphasized, retrieveFeedbacks(), numberToRetrieve);
	}
	
	class Hidden {
        private static Optional<Feedback> getFeedbackByFilter(Predicate<Feedback> tester, Optional<List<Feedback>> feedbacks) {
        	return feedbacks.map(list -> list.stream().filter(tester).findFirst()).orElseGet(() -> Optional.empty());
        }
        
        private static Optional<List<Feedback>> getFeedbacksOrderedBy(Comparator<Feedback> comparator, Optional<List<Feedback>> feedbacks) {
        	return getFeedbacksFilteredAndOrderedBy((feedback) -> true, comparator, feedbacks);
        }
        
        private static Optional<List<Feedback>> getFeedbacksFilteredAndOrderedBy(Predicate<Feedback> tester, Comparator<Feedback> comparator, Optional<List<Feedback>> feedbacks) {
        	return feedbacks.map(list -> list.stream().filter(tester).sorted(comparator).collect(Collectors.toList()));
        }
        
        private static Optional<List<Feedback>> getRandomlyNFeedbacks(Predicate<Feedback> tester, Optional<List<Feedback>> feedbacks, int numberOfElements) {
        	return feedbacks.map(list -> list.stream().filter(tester).sorted((f1, f2) -> (new Random().nextInt(1)) == 0 ? -1 : 1).limit(numberOfElements).collect(Collectors.toList()));
        }
    }
}
