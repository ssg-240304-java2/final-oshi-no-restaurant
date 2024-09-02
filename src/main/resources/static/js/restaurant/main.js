function showCustomerInfo(usageType, numberOfPeople, usageTime, salesDate){
    document.getElementById('customerName').textContent = '고객명: ' + name;
    document.getElementById('customerContact').textContent = '연락처: 010-0000-0000';
    document.getElementById('usageType').textContent = '이용방식: ' + usageType;
    document.getElementById('numberOfPeople').textContent = '인원수: ' + numberOfPeople;
    document.getElementById('usageCount').textContent = '이용횟수: 1';
}

$('.tableData').on('click',function (){

    console.log($(this).children())
    // 클릭한 행에 대한 처리
    let customerName = $(this).children()[0].value;
    let customerContact = $(this).children()[1].value;
    let usageType = $(this).children()[2].value;
    let numberOfPeople = $(this).children()[3].value;
    let usageCount = $(this).children()[4].value;

    // 값을 출력하거나 다른 작업 수행
    console.log({
        customerName: customerName,
        customerContact: customerContact,
        usageType: usageType,
        numberOfPeople: numberOfPeople,
        usageCount: usageCount
    });

    document.getElementById('customerName').textContent = customerName;
    document.getElementById('customerContact').textContent = customerContact;
    document.getElementById('usageType').textContent = usageType;
    document.getElementById('numberOfPeople').textContent = numberOfPeople;
    document.getElementById('usageCount').textContent = usageCount;
});