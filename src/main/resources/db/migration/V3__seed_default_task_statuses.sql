INSERT INTO task_statuses (name, slug)
SELECT v.name, v.slug
FROM (
         VALUES
             ('Draft','draft'),
             ('ToReview','to_review'),
             ('ToBeFixed','to_be_fixed'),
             ('ToPublish','to_publish'),
             ('Published','published')
     ) AS v(name, slug)
         LEFT JOIN task_statuses t ON t.slug = v.slug
WHERE t.slug IS NULL;
