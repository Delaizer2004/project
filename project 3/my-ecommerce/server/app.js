const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const pool = require('./db');

const app = express();
const PORT = process.env.PORT || 3000;

// Обслуговування статичних файлів
app.use(express.static(path.join(__dirname, '../public')));

app.use(bodyParser.json());

// Основні маршрути для HTML
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/index.html'));
});

app.get('/products', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/products.html'));
});

app.get('/cart', (req, res) => {
    res.sendFile(path.join(__dirname, '../public/cart.html'));
});

// API для продуктів
app.get('/api/products', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM products');
        res.json(result.rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server error');
    }
});

// API для оформлення замовлення
app.post('/api/checkout', async (req, res) => {
    try {
        const { cart } = req.body;
        if (!cart || cart.length === 0) {
            return res.status(400).send('Cart is empty');
        }
        const result = await pool.query(
            'INSERT INTO orders (products, total_price) VALUES ($1, $2) RETURNING *',
            [JSON.stringify(cart), cart.reduce((total, product) => total + product.price, 0)]
        );
        res.status(201).json(result.rows[0]);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server error');
    }
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
