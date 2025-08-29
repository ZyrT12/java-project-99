package hexlet.code.spec;

import hexlet.code.model.Task;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> titleContains(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String like = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), like);
    }

    public static Specification<Task> assigneeIdEquals(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.join("assignee", JoinType.LEFT).get("id"), assigneeId);
    }

    public static Specification<Task> statusSlugOrNameEquals(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String val = value.toLowerCase();
        return (root, query, cb) -> cb.or(
                cb.equal(cb.lower(root.join("taskStatus").get("slug")), val),
                cb.equal(cb.lower(root.join("taskStatus").get("name")), val)
        );
    }

    public static Specification<Task> hasLabel(Long labelId) {
        if (labelId == null) {
            return null;
        }
        return (root, query, cb) -> {
            var labels = root.join("labels", JoinType.LEFT);
            query.distinct(true);
            return cb.equal(labels.get("id"), labelId);
        };
    }
}
