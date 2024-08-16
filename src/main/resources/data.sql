MERGE INTO mpa_ratings AS target
USING (VALUES
    (1, 'G', 'no age restrictions for the movie'),
    (2, 'PG', 'parental guidance is recommended for children'),
    (3, 'PG-13', 'not recommended for children under 13'),
    (4, 'R', 'under 17 requires accompanying parent or adult guardian'),
    (5, 'NC-17', 'no one 17 and under admitted')
) AS source (id, name, description)
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET target.name = source.name, target.description = source.description
WHEN NOT MATCHED THEN
    INSERT (id, name, description)
    VALUES (source.id, source.name, source.description);

MERGE INTO genres AS target
USING (VALUES
    (1, 'Comedy'),
    (2, 'Drama'),
    (3, 'Animation'),
    (4, 'Thriller'),
    (5, 'Documentary'),
    (6, 'Action')
) AS source (id, name)
ON target.id = source.id
WHEN MATCHED THEN
    UPDATE SET target.name = source.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (source.id, source.name);
