package hexlet.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = false)
    private String slug;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Label() {
    }

    public Label(String name) {
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    private void ensureSlug() {
        if (this.slug == null || this.slug.isBlank()) {
            this.slug = slugify(this.name);
        }
    }

    private String slugify(String source) {
        if (source == null) {
            return "label";
        }
        String nowhitespace = source.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\p{Alnum}-]").matcher(normalized).replaceAll("");
        slug = slug.replaceAll("-{2,}", "-");
        slug = slug.replaceAll("^-|-$", "");
        if (slug.isBlank()) {
            slug = "label";
        }
        return slug.toLowerCase(Locale.ROOT);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Label)) {
            return false;
        }
        Label label = (Label) o;
        if (id != null && label.id != null) {
            return Objects.equals(id, label.id);
        }
        return Objects.equals(slug, label.slug);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : Objects.hash(slug);
    }
}
