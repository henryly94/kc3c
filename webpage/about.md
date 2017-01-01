---
layout: page
title: 成员介绍
permalink: /about/
---

---

{% if site.data.people %}

{% for person in site.data.people %}

**{{person.name}}**

**Email**: [{{person.email}}](mailto:{{person.email}})

---

{% endfor %}

{% endif %}
