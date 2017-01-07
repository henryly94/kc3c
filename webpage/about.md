---
layout: page
title: 成员介绍
permalink: /about/
---

---

{% if site.data.people %}

{% for person in site.data.people %}

<!-- TODO CHANGE TO Responsive -->

<img src="{{person.photo}}" alt="{{person.name}}" class="photo" />

**{{person.name}}** ([{{person.email}}](mailto:{{person.email}}))

{{person.role}}

{{person.feeling}}

---

{% endfor %}

{% endif %}
