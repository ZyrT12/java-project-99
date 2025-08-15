insert into task_statuses (name, slug)
values ('Draft','draft')
    on conflict (slug) do nothing;

insert into task_statuses (name, slug)
values ('ToReview','to_review')
    on conflict (slug) do nothing;

insert into task_statuses (name, slug)
values ('ToBeFixed','to_be_fixed')
    on conflict (slug) do nothing;

insert into task_statuses (name, slug)
values ('ToPublish','to_publish')
    on conflict (slug) do nothing;

insert into task_statuses (name, slug)
values ('Published','published')
    on conflict (slug) do nothing;
