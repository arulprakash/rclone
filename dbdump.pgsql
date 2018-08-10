--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE comments (
    id bigint NOT NULL,
    description text,
    votes integer,
    created time without time zone,
    changed time without time zone,
    posted_to bigint,
    replied_to bigint,
    commented_by character varying(50)
);


ALTER TABLE comments OWNER TO arul;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: arul
--

CREATE SEQUENCE comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE comments_id_seq OWNER TO arul;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: arul
--

ALTER SEQUENCE comments_id_seq OWNED BY comments.id;


--
-- Name: groups; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE groups (
    id bigint NOT NULL,
    description text,
    rules text,
    created time without time zone,
    changed time without time zone,
    created_by character varying(50)
);


ALTER TABLE groups OWNER TO arul;

--
-- Name: groups_id_seq; Type: SEQUENCE; Schema: public; Owner: arul
--

CREATE SEQUENCE groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE groups_id_seq OWNER TO arul;

--
-- Name: groups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: arul
--

ALTER SEQUENCE groups_id_seq OWNED BY groups.id;


--
-- Name: posts; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE posts (
    id bigint NOT NULL,
    title character varying(30),
    url character varying(2083),
    description text,
    votes integer,
    created time without time zone,
    changed time without time zone,
    posted_by character varying(50),
    posted_in bigint
);


ALTER TABLE posts OWNER TO arul;

--
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public; Owner: arul
--

CREATE SEQUENCE posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE posts_id_seq OWNER TO arul;

--
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: arul
--

ALTER SEQUENCE posts_id_seq OWNED BY posts.id;


--
-- Name: privileges; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE privileges (
    id bigint NOT NULL,
    subscription bigint,
    privilege character varying(50),
    created time without time zone
);


ALTER TABLE privileges OWNER TO arul;

--
-- Name: privileges_id_seq; Type: SEQUENCE; Schema: public; Owner: arul
--

CREATE SEQUENCE privileges_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE privileges_id_seq OWNER TO arul;

--
-- Name: privileges_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: arul
--

ALTER SEQUENCE privileges_id_seq OWNED BY privileges.id;


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE schema_migrations (
    id bigint NOT NULL,
    applied timestamp without time zone,
    description character varying(1024)
);


ALTER TABLE schema_migrations OWNER TO arul;

--
-- Name: subscriptions; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE subscriptions (
    id bigint NOT NULL,
    subscribed_to bigint,
    subscriber character varying(50),
    created time without time zone
);


ALTER TABLE subscriptions OWNER TO arul;

--
-- Name: subscriptions_id_seq; Type: SEQUENCE; Schema: public; Owner: arul
--

CREATE SEQUENCE subscriptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE subscriptions_id_seq OWNER TO arul;

--
-- Name: subscriptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: arul
--

ALTER SEQUENCE subscriptions_id_seq OWNED BY subscriptions.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: arul
--

CREATE TABLE users (
    id character varying(50) NOT NULL,
    first_name character varying(30),
    last_name character varying(30),
    email character varying(50),
    admin boolean,
    created time without time zone,
    changed time without time zone,
    is_active boolean,
    pass character varying(300)
);


ALTER TABLE users OWNER TO arul;

--
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: arul
--

ALTER TABLE ONLY comments ALTER COLUMN id SET DEFAULT nextval('comments_id_seq'::regclass);


--
-- Name: groups id; Type: DEFAULT; Schema: public; Owner: arul
--

ALTER TABLE ONLY groups ALTER COLUMN id SET DEFAULT nextval('groups_id_seq'::regclass);


--
-- Name: posts id; Type: DEFAULT; Schema: public; Owner: arul
--

ALTER TABLE ONLY posts ALTER COLUMN id SET DEFAULT nextval('posts_id_seq'::regclass);


--
-- Name: privileges id; Type: DEFAULT; Schema: public; Owner: arul
--

ALTER TABLE ONLY privileges ALTER COLUMN id SET DEFAULT nextval('privileges_id_seq'::regclass);


--
-- Name: subscriptions id; Type: DEFAULT; Schema: public; Owner: arul
--

ALTER TABLE ONLY subscriptions ALTER COLUMN id SET DEFAULT nextval('subscriptions_id_seq'::regclass);


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY comments (id, description, votes, created, changed, posted_to, replied_to, commented_by) FROM stdin;
1	Comment 1	12	\N	\N	6	\N	arulpugazh
28	Hello Dear	1	08:37:33.238	\N	3	1	arulpugazh
\.


--
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY groups (id, description, rules, created, changed, created_by) FROM stdin;
1	Workout	\N	\N	\N	100
2	Clojure	\N	\N	\N	100
\.


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY posts (id, title, url, description, votes, created, changed, posted_by, posted_in) FROM stdin;
3	ms,d,mds	,msdfmsd,	d,sfmms	1	\N	\N	100	1
4	sdsdsda	sdasda	sdasd	\N	\N	\N	100	1
5	sdsdsda	sdasda	sdasd	\N	\N	\N	100	1
6	fdsfsfsd	fsdfd		\N	22:41:22.601	\N	100	1
7	Hello fslkmfskm	Hello Reddit		\N	22:42:03.436	\N	100	1
8	Hacker News	https://news.ycombinator.com/news		\N	08:27:24.858	\N	100	1
9	Hacker News	https://news.ycombinator.com/news		\N	08:28:30.516	\N	100	1
10	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
11	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
12	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
13	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
14	Test	url	desc	\N	08:27:24.858	\N	100	1
15	Hacker News	https://news.ycombinator.com/news		\N	08:36:21.141	\N	100	1
16	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
17	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
18	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
19	Test	sdsfsdf	sdsadas	\N	08:31:22.753	\N	100	1
20	sfsd	url	description	\N	08:56:34.237	\N	100	1
21	Hacker News	https://news.ycombinator.com/news		\N	08:57:38.866	\N	100	1
22	Hacker News	https://news.ycombinator.com/news		\N	08:58:17.463	\N	100	1
23	title	url	description	\N	22:32:46.559	\N	100	1
24	title	url	description	\N	22:32:46.559	\N	100	1
25	Hacker News	https://news.ycombinator.com/news		\N	22:33:55.855	\N	100	1
26	Hacker News	https://news.ycombinator.com/news		\N	22:34:10.122	\N	100	1
27	Test	url	desc	\N	08:27:24.858	\N	100	1
28	title	url	description	\N	21:14:49.543	\N	100	1
29	title	url	description	\N	21:15:26.912	\N	100	1
30	title	url	description	\N	21:15:52.208	\N	100	1
31	title	url	description	\N	21:16:07.291	\N	100	1
32	title	url	description	\N	21:16:23.244	\N	100	1
33	title	url	description	\N	21:16:37.161	\N	100	1
34	title	url	description	\N	21:17:20.621	\N	100	1
35	title	url	description	\N	21:17:56.029	\N	100	1
36	title	url	description	\N	21:18:17.073	\N	100	1
37	title	url	description	\N	08:37:13.162	\N	100	1
38	fdsfaf	dsdsds		\N	08:37:44.652	\N	100	1
39	fdsfaf	dsdsds		\N	08:38:15.262	\N	100	1
40	fdsfaf	dsdsds		\N	08:44:09.38	\N	100	1
41	title	url	description	\N	08:32:51.749	\N	100	1
\.


--
-- Data for Name: privileges; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY privileges (id, subscription, privilege, created) FROM stdin;
\.


--
-- Data for Name: schema_migrations; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY schema_migrations (id, applied, description) FROM stdin;
20170616213159	2018-03-11 22:20:14.267	add-users-table
\.


--
-- Data for Name: subscriptions; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY subscriptions (id, subscribed_to, subscriber, created) FROM stdin;
1	1	arulpugazh	\N
2	2	arulpugazh	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: arul
--

COPY users (id, first_name, last_name, email, admin, created, changed, is_active, pass) FROM stdin;
100	ARUL	PUGAZH	SJDNKSKN	\N	\N	\N	t	vetri
arulpugazh	Arul	Pugazhendi	hh@hh.com	t	\N	\N	t	vetri
\.


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: arul
--

SELECT pg_catalog.setval('comments_id_seq', 28, true);


--
-- Name: groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: arul
--

SELECT pg_catalog.setval('groups_id_seq', 2, true);


--
-- Name: posts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: arul
--

SELECT pg_catalog.setval('posts_id_seq', 41, true);


--
-- Name: privileges_id_seq; Type: SEQUENCE SET; Schema: public; Owner: arul
--

SELECT pg_catalog.setval('privileges_id_seq', 1, false);


--
-- Name: subscriptions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: arul
--

SELECT pg_catalog.setval('subscriptions_id_seq', 2, true);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: privileges privileges_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY privileges
    ADD CONSTRAINT privileges_pkey PRIMARY KEY (id);


--
-- Name: schema_migrations schema_migrations_id_key; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY schema_migrations
    ADD CONSTRAINT schema_migrations_id_key UNIQUE (id);


--
-- Name: subscriptions subscriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY subscriptions
    ADD CONSTRAINT subscriptions_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: comments comments_commented_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_commented_by_fkey FOREIGN KEY (commented_by) REFERENCES users(id);


--
-- Name: comments comments_posted_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_posted_to_fkey FOREIGN KEY (posted_to) REFERENCES posts(id);


--
-- Name: comments comments_replied_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_replied_to_fkey FOREIGN KEY (replied_to) REFERENCES comments(id);


--
-- Name: groups groups_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_created_by_fkey FOREIGN KEY (created_by) REFERENCES users(id);


--
-- Name: posts posts_posted_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_posted_by_fkey FOREIGN KEY (posted_by) REFERENCES users(id);


--
-- Name: posts posts_posted_in_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_posted_in_fkey FOREIGN KEY (posted_in) REFERENCES groups(id);


--
-- Name: privileges privileges_subscription_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY privileges
    ADD CONSTRAINT privileges_subscription_fkey FOREIGN KEY (subscription) REFERENCES subscriptions(id);


--
-- Name: subscriptions subscriptions_subscribed_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY subscriptions
    ADD CONSTRAINT subscriptions_subscribed_to_fkey FOREIGN KEY (subscribed_to) REFERENCES groups(id);


--
-- Name: subscriptions subscriptions_subscriber_fkey; Type: FK CONSTRAINT; Schema: public; Owner: arul
--

ALTER TABLE ONLY subscriptions
    ADD CONSTRAINT subscriptions_subscriber_fkey FOREIGN KEY (subscriber) REFERENCES users(id);


--
-- PostgreSQL database dump complete
--

