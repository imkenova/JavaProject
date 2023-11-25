let reverse = 0
let order_name = ['Сначала старые', 'Сначала новые']

function sortRows() {
    let table = document.getElementById('1')
    let tBody = table.querySelector('tbody')
    let rows_list = tBody.querySelectorAll('tr')
    let rows = Array.prototype.slice.call(rows_list, 0)
    console.log(rows)
    rows.sort(function(a, b) {
        a_date = new Date(Array.prototype.slice.call(a.childNodes, 0).at(-4).innerHTML)
        b_date = new Date(Array.prototype.slice.call(b.childNodes, 0).at(-4).innerHTML)
        return a_date - b_date
    })
    if (reverse) rows = rows.reverse()
    console.log(reverse)
    tBody.innerHTML = ''
    for (let i = 0; i < rows.length; i++) {
    tBody.appendChild(rows[i])
    }
}
document.getElementById('apply').addEventListener('click', sortRows)

function changeOrder() {
    reverse = (reverse+1)%2
    document.getElementById('order').innerHTML = order_name[reverse]
}
document.getElementById('order').addEventListener('click', changeOrder)
