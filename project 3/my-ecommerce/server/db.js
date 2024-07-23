const { Pool } = require('pg');

const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'site',
    password: '2420',
    port: 5432,
});

module.exports = pool;
